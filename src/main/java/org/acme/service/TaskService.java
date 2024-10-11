package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.task.TaskAdminResponseDTO;
import org.acme.dto.task.TaskCreateDTO;
import org.acme.dto.task.TaskResponseDTO;
import org.acme.dto.task.TaskUpdateDTO;
import org.acme.model.Account;
import org.acme.model.Task;
import org.acme.repository.TaskRepository;
import org.acme.service.impl.ITaskService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@ApplicationScoped
public class TaskService implements ITaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    AccountService accountService;

    @Inject
    JsonWebToken jwt;

    //trova tutti i task controllando se sei admin o utente.
    public PageResult<TaskAdminResponseDTO> findTasks(BaseSearchRequest request, Boolean completeFilter) {

        String queryString  = "";
        boolean firstQueryFlag= true;

        if(completeFilter != null){
            queryString+=  String.format("isComplete = %b",completeFilter);
            firstQueryFlag = false;
        }
        boolean isAdmin = jwt.getGroups().contains("Admin");
        if (!isAdmin) {
            if(!firstQueryFlag){
                queryString += " AND ";
            }
            long userId  = Long.parseLong(jwt.getSubject());
            queryString += String.format(" account.id = %s ", userId);
        }

        if(StringUtils.isNotBlank(request.getSort())){
            queryString += String.format(" order by %s", request.getSort());
        } else {
            queryString += " order by id";
        }

        //panache prepara la query  ma ancora non la esegue!
        PanacheQuery<Task> query = this.taskRepository.find(queryString);
        query.page(request.toPage());

        List<Task> tasks = query.list(); //il .list o firstresult etc, avvisa panache di avviare l'interazione col db

        Page page = query.page();
        List<TaskAdminResponseDTO> dtoList = tasks.stream().map(TaskAdminResponseDTO::of).toList();
        return PageResult.of(dtoList, page.index, page.size, query.count());
    }

    //trova i task per id, se è utente essi devono essere legati a loro
    public TaskResponseDTO getTaskById(long id) {

        Task task = getTaskorThrow(id);

        return TaskResponseDTO.of(task);
    }

    @Transactional
    public TaskResponseDTO createTask(TaskCreateDTO createDTO){
        long userId = Long.parseLong(jwt.getSubject());

        checkTaskExist(createDTO.getTitle(),userId);

        Task newTask = createDTO.toEntity();
        Account account = accountService.getAccountOrThrow(userId);
        newTask.setAccount(account);

        taskRepository.persist(newTask);

        return TaskResponseDTO.of(newTask);
    }

    @Transactional
    public TaskResponseDTO updateTask(long id, TaskUpdateDTO updateDTO){

        Task task = getTaskorThrow(id);

        if (StringUtils.isNotBlank(updateDTO.getTitle())) {
            task.setTitle(updateDTO.getTitle());
        }

        if (StringUtils.isNotBlank(updateDTO.getDescription())) {
            task.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getIsComplete() != null) {
            task.setComplete(updateDTO.getIsComplete());
        }

        taskRepository.persist(task);

        return TaskResponseDTO.of(task);
    }

    @Transactional
    public TaskResponseDTO flagTaskComplete(long id){
        Task task = getTaskorThrow(id);
        task.setComplete(true);
        taskRepository.persist(task);
        return TaskResponseDTO.of(task);
    }

    @Transactional
    public TaskResponseDTO flagTaskInComplete(long id){
        Task task = getTaskorThrow(id);
        task.setComplete(false);
        taskRepository.persist(task);
        return TaskResponseDTO.of(task);
    }

    @Transactional
    public TaskResponseDTO deleteTask(long id){

        Task task = getTaskorThrow(id);

        taskRepository.delete(task);

        return  TaskResponseDTO.of(task);
    }

    private void checkTaskExist(String title,long userId){
        this.taskRepository.find("title = ?1 AND account.id = ?2",title,userId).firstResultOptional().ifPresent(
                task -> {
                    throw new EntityExistsException("Task with title '" + title + "' already exists.");
                }
        );
    }

    private Task getTaskorThrow(long id){
        String query = String.format("id = %s ",id);
        boolean isAdmin = jwt.getGroups().contains("Admin");

        if (!isAdmin) {
            long userId  = Long.parseLong(jwt.getSubject());
            query += String.format(" AND account.id = %s", userId);
        }

        return taskRepository.find(query)
                                                 .firstResultOptional()
                                                 .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + " does not exist!"));

    }
}



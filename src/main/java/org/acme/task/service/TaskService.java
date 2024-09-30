package org.acme.task.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.acme.task.dto.TaskCreateDTO;
import org.acme.task.dto.TaskResponseDTO;
import org.acme.task.dto.TaskUpdateDTO;
import org.acme.task.mapper.TaskMapper;
import org.acme.task.model.Task;
import org.acme.task.repository.TaskRepository;
import org.acme.utils.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped // viene messo nel servies in modo che la performance migliori in quanto verrà caricato solamente 1 volta all'inizio dell'app.  Essendo creato una sola volta si garantisce che ovunque sia iniettato sia lo stesos srvice quindi lo stato è condiviso. assicura che i method flaggati con @transation utilizzano le stesse transation nel corso della loro vita
public class TaskService implements   ITaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    TaskMapper taskMapper;


    public List<TaskResponseDTO> getAllTask(){

            List<Task> allTasks = this.taskRepository.listAll();
            return this.taskMapper.toDtoListResponse(allTasks);

    }

    @Transactional //indica che il code deve essere eseguito all'intero di una transaction, ovvero  quando viene evocato il method se una transizione è gia attiva continua ad utilizzare la stessa e non ne crea una nuova. Si deve usare la transaction per qualsiasi cosa che modifichi lo status del server (put,delete,post etc). La transizione viene "commit"  ovvero salva le modifiche solo SE termina con successo ovvero non throwa exception . In caso di exception effettua un rollback  ovvero annulal tutte le modifiche fatte durante la transizione, in questo modo l'integrita strutturale del DB viene preservata.
    public TaskResponseDTO createTask (TaskCreateDTO createDTO){
            //pulizia dei valori d'ingresso, in questo caso impsotiamo la prima elttera del titolo in upperCase. Solo la priam lettera in quanto potremmo trovare nei titoli delle sigle in maiuscolo e devono restare tali.
            String title = StringUtils.capitalizeOnlyFirstLetter(createDTO.getTitle());

            Optional<Task> TaskCheckOpt = this.taskRepository.findByTitle(title);
            if (TaskCheckOpt.isPresent()) throw new EntityExistsException("Task with title '" + title + "' already exists.");
            //se il titolo non esiste alllora impostiamo il testo del titol omdoificato al createDTO per prepararlo al salvataggio.
            createDTO.setTitle(title);
            Task newTask = this.taskMapper.toEntityCreate(createDTO);

            //persist = salva nel db
            this.taskRepository.persist(newTask);

            //hibernate è cosi potente da aggiornare automaticamente l'oggetto newtask con i valori autogenerati dopo il persist, in questo modo quando rinviamo indietro l'oggetto newTask esso sarà completo di tutte le informazioni necessarie.
            return taskMapper.toDtoResponse(newTask);

    }

    public TaskResponseDTO findByTitle(String title){

            String titleCapitalized = StringUtils.capitalizeOnlyFirstLetter(title);
            if (title == null || title.isEmpty() ) throw new IllegalArgumentException("Title cannot be empty");
            Optional<Task> taskOpt = this.taskRepository.findByTitle(title);
            if(!taskOpt.isPresent()) throw new NoSuchElementException("Task with Title: " + title + " not exist!");
            Task task = taskOpt.get();
            return this.taskMapper.toDtoResponse(task);

    }

    public TaskResponseDTO findById(long id){


            if (id <= 0) throw new IllegalArgumentException("ID non valido");

            Optional<Task> TaskCheckOpt = this.taskRepository.findByIdOptional(id);
            if (!TaskCheckOpt.isPresent()) throw new NoSuchElementException("Task with id: '" + id + "' not exists.");

            Task task = TaskCheckOpt.get();

            return this.taskMapper.toDtoResponse(task);

    }

    @Transactional
    public TaskResponseDTO updateTask(long id, TaskUpdateDTO updateDTO) {

            if (id <= 0) throw new IllegalArgumentException("Invalid ID value");

            Optional<Task> TaskCheckOpt = this.taskRepository.findByIdOptional(id);
            if (!TaskCheckOpt.isPresent()) throw new NoSuchElementException("Task with id: '" + id + "' not exists.");

            Task updateTask = TaskCheckOpt.get(); // online ho eltto che conviene sempre estrarre il value del opt in un altra variabile e non usare il .get direttamente.

            //Valutare in futuro di creare una classe Utils per controllo ed inserimento automatico di questi valori, per ora essendo solo 3 vabene cosi!
            if(updateDTO.getTitle() != null && !updateDTO.getTitle().isEmpty())
                updateTask.setTitle(StringUtils.capitalizeOnlyFirstLetter(updateDTO.getTitle())) ;

            if(updateDTO.getDescription() != null && !updateDTO.getDescription().isEmpty())
                updateTask.setDescription(updateDTO.getDescription());

            if(updateDTO.isComplete() != null)
                updateTask.setComplete(updateDTO.isComplete());

            this.taskRepository.persist(updateTask);

            return this.taskMapper.toDtoResponse(updateTask);

    }

    @Transactional
    public TaskResponseDTO deleteById(long id){

            if (id <= 0) throw new IllegalArgumentException("Invalid ID value!");

            Optional<Task> TaskCheckOpt = this.taskRepository.findByIdOptional(id);
            if (!TaskCheckOpt.isPresent()) throw new EntityExistsException("Task with id: '" + id + "' not exists.");

            Task task = TaskCheckOpt.get();

            this.taskRepository.delete(task);

            return this.taskMapper.toDtoResponse(task);

    }


    public List<TaskResponseDTO> findByCompletion (String booleanValue){

            if(booleanValue == null || booleanValue.isEmpty())
                throw new IllegalArgumentException("ID non valido");

            //il parsing da String a boolean otterrà true solo se la stringa conterrà "true" altrimenti false
            boolean isComplete = Boolean.parseBoolean(booleanValue);

            List<Task> filterTask = this.taskRepository.findByCompletion(isComplete);

            return this.taskMapper.toDtoListResponse(filterTask);

    }
}

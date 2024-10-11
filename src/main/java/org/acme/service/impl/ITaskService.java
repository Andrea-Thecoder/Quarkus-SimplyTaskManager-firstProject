package org.acme.service.impl;

import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.task.TaskAdminResponseDTO;
import org.acme.dto.task.TaskCreateDTO;
import org.acme.dto.task.TaskResponseDTO;
import org.acme.dto.task.TaskUpdateDTO;

public interface ITaskService {

    PageResult<TaskAdminResponseDTO> findTasks(BaseSearchRequest request, Boolean completeFilter);
    TaskResponseDTO getTaskById(long id);
    TaskResponseDTO createTask(TaskCreateDTO createDTO);
    TaskResponseDTO updateTask(long id, TaskUpdateDTO updateDTO);
    TaskResponseDTO flagTaskComplete(long id);
    TaskResponseDTO flagTaskInComplete(long id);
    TaskResponseDTO deleteTask(long id);




}

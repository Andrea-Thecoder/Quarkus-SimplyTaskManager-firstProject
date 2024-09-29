package org.acme.task.service;

import org.acme.task.dto.TaskCreateDTO;
import org.acme.task.dto.TaskResponseDTO;
import org.acme.task.dto.TaskUpdateDTO;

import java.util.List;

public interface ITaskService {

    List<TaskResponseDTO> getAllTask();
    TaskResponseDTO createTask(TaskCreateDTO createDTO);
    TaskResponseDTO findByTitle(String title);
    TaskResponseDTO findById(long id);
    List<TaskResponseDTO> findByCompletion(String booleanValue);
    TaskResponseDTO updateTask (long id, TaskUpdateDTO updateDTO);
    TaskResponseDTO deleteById(long id);
}

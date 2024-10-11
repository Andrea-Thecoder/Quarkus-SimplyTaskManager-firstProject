package org.acme.dto.task;


import lombok.Getter;
import lombok.Setter;
import org.acme.model.Task;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDTO {

    private long id;

    private String title;

    private String description;

    private LocalDateTime createAt;

    private boolean isComplete;

    public static TaskResponseDTO of(Task task) {

    TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreateAt(task.getCreateAt());
        dto.setComplete(task.isComplete());
        return dto;
    }
}
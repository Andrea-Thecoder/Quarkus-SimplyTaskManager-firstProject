package org.acme.dto.task;

import lombok.Getter;
import org.acme.model.Task;

@Getter
public class TaskAdminResponseDTO extends TaskResponseDTO{

    private long accountId;

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public static TaskAdminResponseDTO of (Task task){
        TaskAdminResponseDTO dto = new TaskAdminResponseDTO();
        TaskResponseDTO baseDto = TaskResponseDTO.of(task);
        dto.setId(baseDto.getId());
        dto.setTitle(baseDto.getTitle());
        dto.setDescription(baseDto.getDescription());
        dto.setCreateAt(baseDto.getCreateAt());
        dto.setComplete(baseDto.isComplete());
        dto.setAccountId(task.getAccount().getId());

        return dto;
    }
}

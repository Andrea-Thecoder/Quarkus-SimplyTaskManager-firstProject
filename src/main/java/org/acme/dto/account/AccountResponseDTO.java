package org.acme.dto.account;

import lombok.Getter;
import lombok.Setter;
import org.acme.dto.task.TaskResponseDTO;
import org.acme.model.Account;

import java.util.List;

@Setter @Getter
public class AccountResponseDTO {

    private long id;

    private String username;

    private String email;

    private String roleName;

    private List<TaskResponseDTO> tasks;

    public static AccountResponseDTO of (Account account){
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setUsername(account.getUsername());
        dto.setEmail(account.getEmail());
        dto.setRoleName(account.getRole().getRoleName());
        List<TaskResponseDTO> dtoTasks = account.getTasks()
                                                                         .stream()
                                                                         .map(TaskResponseDTO::of).toList();
        dto.setTasks(dtoTasks);
        return dto;
    }


}

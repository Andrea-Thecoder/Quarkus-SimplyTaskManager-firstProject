package org.acme.dto.role;


import lombok.Getter;
import lombok.Setter;
import org.acme.model.AccountRole;

@Getter @Setter
public class AccountRoleResponseDTO {

    private long id;

    private String roleName;

    public static AccountRoleResponseDTO of (AccountRole role){
        AccountRoleResponseDTO dto = new AccountRoleResponseDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        return dto;
    }


}

package org.acme.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.acme.model.AccountRole;
import org.acme.utils.StringUtils;

@Setter @Getter
public class AccountRoleDTO {

    @NotBlank(message = "Role name cannot be empty")
    @Size( max = 20 ,message = "Role name must be max 20 characters")
    private String roleName;

    public void setRoleName( String roleName) {
        this.roleName = StringUtils.capitalizeFirstLetter(roleName);
    }

    public AccountRole toEntity(){
        AccountRole role = new AccountRole();
        role.setRoleName(this.roleName);
        return role;
    }

}

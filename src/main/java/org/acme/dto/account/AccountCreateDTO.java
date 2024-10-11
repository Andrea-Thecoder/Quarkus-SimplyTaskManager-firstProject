package org.acme.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.acme.dto.profile.ProfileCreateDTO;
import org.acme.model.Account;
import org.acme.model.Profile;
import org.acme.utils.StringUtils;

@Getter @Setter
public class AccountCreateDTO {

    @NotBlank(message = "username cannot be empty")
    @Size(max = 30,message = "username must be between 1 and 30 characters")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 16,message = "password must be between 8 and 16 characters")
    private String password;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "invalid email")
    @Size(max = 60, message = "email must be between 8 and 16 characters")
    private String email;

    @Size( max =20 ,message = "Role name must be max 20 characters")
    private String roleName;

    private ProfileCreateDTO profile;

    public void setEmail(  String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setRoleName( String roleName) {
        this.roleName = StringUtils.capitalizeFirstLetter(roleName);
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public Account toEntity(){
        Account account = new Account();
        account.setUsername(this.username);
        account.setPassword(this.password);
        account.setEmail(this.email);
        Profile profileEntity = this.profile.toEntity();
        profileEntity.setAccountId(account);
        account.setProfile(profileEntity);
        return account;

    }



}

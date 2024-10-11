package org.acme.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AccountPasswordUpdateDTO {

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 16,message = "password must be between 8 and 16 characters")
    private String password;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 16,message = "password must be between 8 and 16 characters")
    private String repeatPassword;

    @NotBlank(message = "new password cannot be empty")
    @Size(min = 8, max = 16,message = "new password must be between 8 and 16 characters")
    private String newPassword;

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword.trim();
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
}

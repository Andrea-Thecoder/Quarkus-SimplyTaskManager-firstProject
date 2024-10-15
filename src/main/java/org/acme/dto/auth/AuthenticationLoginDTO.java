package org.acme.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthenticationLoginDTO {

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 16,message = "password must be between 8 and 16 characters")
    private String password;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "invalid email")
    @Size(max = 60, message = "email must be between 8 and 16 characters")
    private String email;

    public void setEmail(  String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setPassword( String password) {
        this.password = password.trim();
    }
}

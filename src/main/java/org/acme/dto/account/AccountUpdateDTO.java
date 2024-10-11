package org.acme.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AccountUpdateDTO {

    @NotBlank(message = "username cannot be empty")
    @Size(max = 30,message = "username must be between 1 and 30 characters")
    private String username;

    public void setUsername(String username) {
        this.username = username.trim();
    }
}

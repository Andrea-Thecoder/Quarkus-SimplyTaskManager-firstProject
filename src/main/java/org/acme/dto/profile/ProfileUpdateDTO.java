package org.acme.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.acme.utils.StringUtils;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProfileUpdateDTO {

    @NotBlank(message = "firstname cannot be empty")
    @Size( max = 80,message = "firstname must be between 1 and 30 characters")
    private String firstname;

    @NotBlank(message = "lastname cannot be empty")
    @Size(max = 80,message = "lastname must be between 1 and 30 characters")
    private String lastname;

    @NotBlank(message = "country cannot be empty")
    @Size(max = 35,message = "country must be between 1 and 35 characters")
    private String country;

    @NotBlank(message = "phoneNumber cannot be empty")
    @Size(min = 10, max = 15,message = "phone number mut be between 10 and 15 characters")
    @Pattern(regexp = "^\\+?\\d+$", message = "phone number can only contain number!")
    private String phoneNumber;

    @NotBlank(message = "taxCode cannot be empty")
    @Size(min = 16, max = 16,message = "taxCode must be between 1 and 30 characters")
    private String taxCode;

    @NotNull(message = "birthDate cannot be empty")
    private LocalDate birthDate;

    public void setFirstname(String firstname) {
        this.firstname = StringUtils.capitalizeFirstLetter(firstname);
    }

    public void setLastname(String lastname) {
        this.lastname = StringUtils.capitalizeFirstLetter(lastname);
    }

    public void setCountry(String country) {
        this.country = StringUtils.capitalizeFirstLetter(country);
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode.trim().toUpperCase();
    }
}

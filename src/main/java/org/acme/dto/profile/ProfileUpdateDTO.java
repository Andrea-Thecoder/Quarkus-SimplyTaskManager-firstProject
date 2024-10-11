package org.acme.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.acme.utils.StringUtils;

import java.time.LocalDate;

@Getter @Setter
public class ProfileUpdateDTO {
    @NotBlank(message = "firstname cannot be empty")
    @Size( max = 80,message = "firstname must be between 1 and 30 characters")
    private String firstname;

    @Size(max = 80,message = "lastname must be between 1 and 30 characters")
    private String lastname;

    @Size(max = 35,message = "country must be between 1 and 30 characters")
    private String country;

    @Size(min = 10, max = 15,message = "phone number mut be between 1 and 30 characters")
    @Pattern(regexp = "^\\+?\\d+$\n", message = "phone number can only contain number!")
    private String phoneNumber;

    @Size(min = 16, max = 16,message = "taxCode must be between 1 and 30 characters")
    private String taxCode;

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

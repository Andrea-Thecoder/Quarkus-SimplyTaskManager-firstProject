package org.acme.dto.profile;

import lombok.Getter;
import lombok.Setter;
import org.acme.model.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter @Getter
public class ProfileResponseDTO {

    private String firstname;

    private String lastname;

    private String country;

    private String phoneNumber;

    private String taxCode;

    private LocalDate birthDate;

    private LocalDateTime updateAt;


    public static ProfileResponseDTO of(Profile profile){
        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setFirstname(profile.getFirstname());
        responseDTO.setLastname(profile.getLastname());
        responseDTO.setCountry(profile.getCountry());
        responseDTO.setPhoneNumber(profile.getPhoneNumber());
        responseDTO.setTaxCode(profile.getTaxCode());
        responseDTO.setBirthDate(profile.getBirthDate());
        responseDTO.setUpdateAt(profile.getUpdateAt());
        return responseDTO;

    }
}

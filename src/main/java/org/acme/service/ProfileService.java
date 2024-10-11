package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.acme.dto.profile.ProfileResponseDTO;
import org.acme.dto.profile.ProfileUpdateDTO;
import org.acme.model.Profile;
import org.acme.repository.ProfileRepository;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class ProfileService {

    @Inject
    ProfileRepository profileRepository;

    @Inject
    JsonWebToken jwt;

    public ProfileResponseDTO getProfileById(){
        long userId = Long.parseLong(jwt.getSubject());
        return ProfileResponseDTO.of(getProfileOrThrow(userId));
    }

    public ProfileResponseDTO updateProfile(ProfileUpdateDTO updateDTO){
        long userId = Long.parseLong(jwt.getSubject());
        Profile updateProfile = getProfileOrThrow(userId);

        if (StringUtils.isNotBlank(updateDTO.getFirstname())) {
            updateProfile.setFirstname(updateDTO.getFirstname());
        }
        if (StringUtils.isNotBlank(updateDTO.getLastname())) {
            updateProfile.setLastname(updateDTO.getLastname());
        }
        if (StringUtils.isNotBlank(updateDTO.getCountry())) {
            updateProfile.setCountry(updateDTO.getCountry());
        }
        if (StringUtils.isNotBlank(updateDTO.getPhoneNumber())) {
            updateProfile.setPhoneNumber(updateDTO.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(updateDTO.getTaxCode())) {
            updateProfile.setTaxCode(updateDTO.getTaxCode());
        }
        if (updateDTO.getBirthDate() != null) { // La data non deve essere controllata per blank
            updateProfile.setBirthDate(updateDTO.getBirthDate());
        }
        profileRepository.persist(updateProfile);

        return ProfileResponseDTO.of(updateProfile);

    }


    private Profile getProfileOrThrow(long id){
        return profileRepository.find(" accountId.id = ?1", id).firstResultOptional().orElseThrow(
                ()-> new EntityNotFoundException("Profile associated at account with id: "+id+ " does not exist!")
        );
    }
}

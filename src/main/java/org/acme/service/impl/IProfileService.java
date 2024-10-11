package org.acme.service.impl;

import org.acme.dto.profile.ProfileResponseDTO;
import org.acme.dto.profile.ProfileUpdateDTO;

public interface IProfileService {

    ProfileResponseDTO getProfileById();
    ProfileResponseDTO updateProfile(ProfileUpdateDTO updateDTO);
}

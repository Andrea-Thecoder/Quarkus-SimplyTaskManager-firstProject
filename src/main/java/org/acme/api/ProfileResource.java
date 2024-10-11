package org.acme.api;


import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.profile.ProfileResponseDTO;
import org.acme.dto.profile.ProfileUpdateDTO;
import org.acme.service.ProfileService;
import org.acme.utils.response.SuccessResponse;

@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class ProfileResource {

    @Inject
    ProfileService profileService;

    @GET
    public SuccessResponse<ProfileResponseDTO> getProfileById(){
        return new SuccessResponse<>(profileService.getProfileById());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<ProfileResponseDTO> updateProfile(
            @Valid ProfileUpdateDTO updateDTO
            ){
        return new SuccessResponse<>(profileService.updateProfile(updateDTO));
    }






}

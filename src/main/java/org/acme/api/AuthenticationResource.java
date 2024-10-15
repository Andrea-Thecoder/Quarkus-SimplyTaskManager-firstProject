package org.acme.api;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.auth.AuthenticationLoginDTO;
import org.acme.dto.auth.AuthenticationResponseDTO;
import org.acme.dto.auth.AuthenticationSendTokenDTO;
import org.acme.dto.response.SuccessResponse;
import org.acme.service.AuthenticationService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON) //indica che output(response) questo endpoint produrrà, in questo caso un json!
public class AuthenticationResource {

    @Inject
    AuthenticationService authService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<AuthenticationResponseDTO> login (
                @Valid AuthenticationLoginDTO loginDTO
    ){
        return new SuccessResponse<>(authService.login(loginDTO));
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<String> regenerateAccessToken (
            @Valid AuthenticationSendTokenDTO tokenDTO
            ){
        return new SuccessResponse<>(authService.regenerateAccessToken(tokenDTO));
    }



}

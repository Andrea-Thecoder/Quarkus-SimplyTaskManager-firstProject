package org.acme.api;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.AuthenticationLoginDTO;
import org.acme.service.AuthenticationService;
import org.acme.utils.response.SuccessResponse;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON) //indica che output(response) questo endpoint produrrà, in questo caso un json!
@Consumes(MediaType.APPLICATION_JSON) //indica che Input(request) questo controller accettà in questo caso json.

public class AuthenticationResource {

    @Inject
    AuthenticationService authService;

    @POST
    @Path("/login")
        public SuccessResponse<String> login (
                @Valid AuthenticationLoginDTO loginDTO
    ){
        return new SuccessResponse<>(authService.login(loginDTO));
    }


}

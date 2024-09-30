package org.acme.utils.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.utils.response.ErrorResponse;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.UNAUTHORIZED.getStatusCode(),
                "Unauthorized access",
                exception.getMessage()
        );
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
    }

}

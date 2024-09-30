package org.acme.utils.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.utils.response.ErrorResponse;

@Provider
public class EntityExistsExceptionMapper implements ExceptionMapper<EntityExistsException> {

    @Override
    public Response toResponse(EntityExistsException exception) {
        int status = Response.Status.CONFLICT.getStatusCode(); // 409 Conflict
        String message = "Entity already exists";
        String details = exception.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(status, message, details);
        return Response.status(status).entity(errorResponse).build();
    }
}

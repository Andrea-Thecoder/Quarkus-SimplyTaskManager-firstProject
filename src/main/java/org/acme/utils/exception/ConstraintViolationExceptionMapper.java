package org.acme.utils.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.utils.response.ErrorResponse;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // in questo modo gestiamo il messaggio custom, se presente allora lo mappa e lo prende, altrimenti mette uno di default.
     /*   String errorMessage = exception.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Validation not work");*/

        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Validation not work",
                exception.getMessage()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}

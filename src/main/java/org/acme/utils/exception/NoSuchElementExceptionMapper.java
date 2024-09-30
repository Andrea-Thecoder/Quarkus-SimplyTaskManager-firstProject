package org.acme.utils.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.utils.response.ErrorResponse;

import java.util.NoSuchElementException;

@Provider
public class NoSuchElementExceptionMapper  implements ExceptionMapper<NoSuchElementException> {

    @Override
    public Response toResponse(NoSuchElementException exception) {
        // Imposta il codice di stato HTTP e il messaggio di errore
        int status = Response.Status.NOT_FOUND.getStatusCode();
        String message = "Resource not found";
        String details = exception.getMessage();

        // Crea una risposta con l'oggetto ErrorResponse
        ErrorResponse errorResponse = new ErrorResponse(status, message, details);
        return Response.status(status).entity(errorResponse).build();
    }
}

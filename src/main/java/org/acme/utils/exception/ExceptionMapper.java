package org.acme.utils.exception;

import com.fasterxml.jackson.core.JsonParseException;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import org.acme.utils.response.ErrorResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.util.NoSuchElementException;

@Provider
public class ExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(AuthenticationFailedException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ErrorResponse(Status.UNAUTHORIZED.getStatusCode(), "Authentication failed", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();

    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(ForbiddenException exception) {
        return ResponseBuilder
                .create(Status.FORBIDDEN, new ErrorResponse(Status.FORBIDDEN.getStatusCode(), "Access forbidden", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(EntityExistsException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ErrorResponse(Status.CONFLICT.getStatusCode(), "Entity already exists", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(ConstraintViolationException exception) {
        return ResponseBuilder
                .create(Status.BAD_REQUEST, new ErrorResponse(Status.BAD_REQUEST.getStatusCode(), "Constraint violation", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(IllegalArgumentException exception) {
        return ResponseBuilder
                .create(Status.BAD_REQUEST, new ErrorResponse(Status.BAD_REQUEST.getStatusCode(), "Illegal argument", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(InvalidJwtException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ErrorResponse(Status.UNAUTHORIZED.getStatusCode(), "Invalid JWT token", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(RuntimeException exception) {
        return ResponseBuilder
                .create(Status.INTERNAL_SERVER_ERROR,
                        new ErrorResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "An unexpected error occurred", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(NoSuchElementException exception) {
        return ResponseBuilder
                .create(Status.INTERNAL_SERVER_ERROR,
                        new ErrorResponse(
                                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                "An unexpected error occurred",
                                exception.getMessage())) // Usa il messaggio dell'eccezione
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(NotFoundException exception) {
        return ResponseBuilder
                .create(Status.NOT_FOUND, new ErrorResponse(Status.NOT_FOUND.getStatusCode(), "Resource not found", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(JsonParseException exception) {
        return ResponseBuilder
                .create(Status.BAD_REQUEST, new ErrorResponse(Status.BAD_REQUEST.getStatusCode(), "Error parsing JSON", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(SecurityException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ErrorResponse(Status.UNAUTHORIZED.getStatusCode(), "Security exception", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(InvalidPasswordException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ErrorResponse(Status.UNAUTHORIZED.getStatusCode(), "Invalid password", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(RoleDeleteException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ErrorResponse(Status.CONFLICT.getStatusCode(), "Role deletion conflict", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> toResponse(EntityNotFoundException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ErrorResponse(Status.CONFLICT.getStatusCode(), "Role deletion conflict", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}

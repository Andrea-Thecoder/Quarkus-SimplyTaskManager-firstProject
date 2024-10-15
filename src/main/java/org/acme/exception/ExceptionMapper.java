package org.acme.exception;

import com.fasterxml.jackson.core.JsonParseException;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import org.acme.dto.response.ExceptionResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.util.NoSuchElementException;

@Provider
public class ExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(AuthenticationFailedException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ExceptionResponse(Status.UNAUTHORIZED.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();

    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(ForbiddenException exception) {
        return ResponseBuilder
                .create(Status.FORBIDDEN, new ExceptionResponse(Status.FORBIDDEN.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(EntityExistsException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ExceptionResponse(Status.CONFLICT.getStatusCode(),exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(IllegalArgumentException exception) {
        return ResponseBuilder
                .create(Status.BAD_REQUEST, new ExceptionResponse(Status.BAD_REQUEST.getStatusCode(),exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(InvalidJwtException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ExceptionResponse(Status.UNAUTHORIZED.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(RuntimeException exception) {
        return ResponseBuilder
                .create(Status.INTERNAL_SERVER_ERROR,
                        new ExceptionResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(ParseException exception) {
        return ResponseBuilder
                .create(Status.INTERNAL_SERVER_ERROR,
                        new ExceptionResponse(Status.UNAUTHORIZED.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(NoSuchElementException exception) {
        return ResponseBuilder
                .create(Status.INTERNAL_SERVER_ERROR,
                        new ExceptionResponse(
                                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(NotFoundException exception) {
        return ResponseBuilder
                .create(Status.NOT_FOUND, new ExceptionResponse(Status.NOT_FOUND.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(JsonParseException exception) {
        return ResponseBuilder
                .create(Status.BAD_REQUEST, new ExceptionResponse(Status.BAD_REQUEST.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(SecurityException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ExceptionResponse(Status.UNAUTHORIZED.getStatusCode(),exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(UnauthorizedException exception) {
        return ResponseBuilder
                .create(Status.UNAUTHORIZED, new ExceptionResponse(Status.UNAUTHORIZED.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(RoleDeleteException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ExceptionResponse(Status.CONFLICT.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponse> toResponse(EntityNotFoundException exception) {
        return ResponseBuilder
                .create(Status.CONFLICT, new ExceptionResponse(Status.CONFLICT.getStatusCode(), exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}

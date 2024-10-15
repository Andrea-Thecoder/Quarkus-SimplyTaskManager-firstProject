package org.acme.dto.response;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse<T> {

    private int status;
    private T data;

    public SuccessResponse(T data) {
        this.status = Response.Status.OK.getStatusCode();
        this.data = data;
    }
}

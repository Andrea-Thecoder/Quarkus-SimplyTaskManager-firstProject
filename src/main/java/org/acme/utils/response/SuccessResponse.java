package org.acme.utils.response;

import jakarta.ws.rs.core.Response;

public class SuccessResponse<T> {

    private int status;
    private T data;

    public SuccessResponse(T data) {
        this.status = Response.Status.OK.getStatusCode();
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

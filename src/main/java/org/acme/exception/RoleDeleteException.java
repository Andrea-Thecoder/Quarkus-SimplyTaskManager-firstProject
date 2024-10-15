package org.acme.exception;

public class RoleDeleteException extends RuntimeException {
    public RoleDeleteException(String message) {
        super(message);
    }
}

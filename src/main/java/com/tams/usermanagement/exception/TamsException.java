package com.tams.usermanagement.exception;

public class TamsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TamsException() {
        this("");
    }

    public TamsException(String message) {
        super(message);
    }

    public TamsException(Throwable cause) {
        super(cause);
    }

    public TamsException(String message, Throwable cause) {
        super(message, cause);
    }

}
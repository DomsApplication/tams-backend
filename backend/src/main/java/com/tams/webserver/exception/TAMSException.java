package com.tams.webserver.exception;

public class TAMSException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TAMSException() {
        this("");
    }

    public TAMSException(String message) {
        super(message);
    }

    public TAMSException(Throwable cause) {
        super(cause);
    }

    public TAMSException(String message, Throwable cause) {
        super(message, cause);
    }

}
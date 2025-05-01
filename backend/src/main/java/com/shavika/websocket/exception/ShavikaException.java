package com.shavika.websocket.exception;

public class ShavikaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShavikaException() {
        this("");
    }

    public ShavikaException(String message) {
        super(message);
    }

    public ShavikaException(Throwable cause) {
        super(cause);
    }

    public ShavikaException(String message, Throwable cause) {
        super(message, cause);
    }

}
package com.proj4.exceptions;

public class UnexpectedTypeException extends RuntimeException {
    public UnexpectedTypeException() {
    }

    public UnexpectedTypeException(String msg) {
        super(msg);
    }

    public UnexpectedTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnexpectedTypeException(Throwable cause) {
        super(cause);
    }
}
package com.proj4.exceptions;

public class MismatchedTypeException extends RuntimeException {
    public MismatchedTypeException() {
    }

    public MismatchedTypeException(String msg) {
        super(msg);
    }

    public MismatchedTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MismatchedTypeException(Throwable cause) {
        super(cause);
    }
}

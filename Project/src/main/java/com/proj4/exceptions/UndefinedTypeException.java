package com.proj4.exceptions;

public class UndefinedTypeException extends RuntimeException {
    public UndefinedTypeException() {
    }

    public UndefinedTypeException(String msg) {
        super(msg);
    }

    public UndefinedTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UndefinedTypeException(Throwable cause) {
        super(cause);
    }
}

package com.proj4.exceptions;

public class UndefinedVariableException extends RuntimeException {
    public UndefinedVariableException() {
    }

    public UndefinedVariableException(String msg) {
        super(msg);
    }

    public UndefinedVariableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UndefinedVariableException(Throwable cause) {
        super(cause);
    }
}
package com.proj4.exceptions;

public class VariableAlreadyDefinedException extends RuntimeException {
    public VariableAlreadyDefinedException() {
    }

    public VariableAlreadyDefinedException(String msg) {
        super(msg);
    }

    public VariableAlreadyDefinedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public VariableAlreadyDefinedException(Throwable cause) {
        super(cause);
    }
}

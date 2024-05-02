package com.proj4.exceptions;

public class ParameterMismatchExpection extends RuntimeException {
    public ParameterMismatchExpection() {
    }

    public ParameterMismatchExpection(String msg) {
        super(msg);
    }

    public ParameterMismatchExpection(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ParameterMismatchExpection(Throwable cause) {
        super(cause);
    }
}

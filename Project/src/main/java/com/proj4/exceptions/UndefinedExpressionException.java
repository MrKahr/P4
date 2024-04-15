package com.proj4.exceptions;

public class UndefinedExpressionException extends RuntimeException {
    public UndefinedExpressionException() {
    }

    public UndefinedExpressionException(String msg) {
        super(msg);
    }

    public UndefinedExpressionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UndefinedExpressionException(Throwable cause) {
        super(cause);
    }
}

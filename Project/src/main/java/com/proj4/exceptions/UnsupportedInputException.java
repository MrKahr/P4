package com.proj4.exceptions;

public class UnsupportedInputException extends RuntimeException {
    public UnsupportedInputException() {
    }

    public UnsupportedInputException(String msg) {
        super(msg);
    }

    public UnsupportedInputException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsupportedInputException(Throwable cause) {
        super(cause);
    }
}

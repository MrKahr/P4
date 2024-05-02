package com.proj4.exceptions;

public class MalformedAstException extends RuntimeException {
    public MalformedAstException() {
    }

    public MalformedAstException(String msg) {
        super(msg);
    }

    public MalformedAstException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MalformedAstException(Throwable cause) {
        super(cause);
    }
}

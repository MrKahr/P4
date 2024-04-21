package com.proj4.exceptions;

public class UndefinedActionExpection extends RuntimeException {
    public UndefinedActionExpection() {
    }

    public UndefinedActionExpection(String msg) {
        super(msg);
    }

    public UndefinedActionExpection(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UndefinedActionExpection(Throwable cause) {
        super(cause);
    }
}

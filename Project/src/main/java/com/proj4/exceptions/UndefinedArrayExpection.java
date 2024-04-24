package com.proj4.exceptions;

public class UndefinedArrayExpection extends RuntimeException {
    public UndefinedArrayExpection() {
    }

    public UndefinedArrayExpection(String msg) {
        super(msg);
    }

    public UndefinedArrayExpection(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UndefinedArrayExpection(Throwable cause) {
        super(cause);
    }
}
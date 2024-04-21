package com.proj4.exceptions;

public class StateAlreadyDefinedExpection extends RuntimeException {
    public StateAlreadyDefinedExpection() {
    }

    public StateAlreadyDefinedExpection(String msg) {
        super(msg);
    }

    public StateAlreadyDefinedExpection(String msg, Throwable cause) {
        super(msg, cause);
    }

    public StateAlreadyDefinedExpection(Throwable cause) {
        super(cause);
    }
}

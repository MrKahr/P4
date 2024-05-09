package com.proj4.exceptions;

public class UnexpectedComparisonExpection extends RuntimeException {
    public UnexpectedComparisonExpection() {
    }

    public UnexpectedComparisonExpection(String msg) {
        super(msg);
    }

    public UnexpectedComparisonExpection(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnexpectedComparisonExpection(Throwable cause) {
        super(cause);
    }
}

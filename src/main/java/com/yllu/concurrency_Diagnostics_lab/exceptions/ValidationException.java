package com.yllu.concurrency_Diagnostics_lab.exceptions;


public class ValidationException extends RuntimeException {

    private final String tag;

    public ValidationException(String tag, String message) {
        super(message);
        this.tag = tag;
    }

    public ValidationException(String tag, String message, Throwable cause) {
        super(message, cause);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

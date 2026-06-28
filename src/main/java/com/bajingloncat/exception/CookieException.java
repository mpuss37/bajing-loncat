package com.bajingloncat.exception;

public class CookieException extends RuntimeException {

    public CookieException(String message) {
        super(message);
    }

    public CookieException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.myApp.exception;

public class ActionDeniedException extends RuntimeException {
    public ActionDeniedException(String message) {
        super(message);
    }
}

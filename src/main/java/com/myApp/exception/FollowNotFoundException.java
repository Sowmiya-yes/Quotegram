package com.myApp.exception;

public class FollowNotFoundException extends RuntimeException {
    public FollowNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

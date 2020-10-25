package com.myApp.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

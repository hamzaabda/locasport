package com.example.project.exception;



public class AlerteNotFoundException extends RuntimeException {
    public AlerteNotFoundException(String message) {
        super(message);
    }
}
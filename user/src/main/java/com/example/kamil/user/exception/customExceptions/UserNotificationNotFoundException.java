package com.example.kamil.user.exception.customExceptions;

public class UserNotificationNotFoundException extends RuntimeException{

    public UserNotificationNotFoundException(String message) {
        super(message);
    }
}


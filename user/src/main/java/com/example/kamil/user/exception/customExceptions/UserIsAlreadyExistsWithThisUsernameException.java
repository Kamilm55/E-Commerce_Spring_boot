package com.example.kamil.user.exception.customExceptions;

public class UserIsAlreadyExistsWithThisUsernameException extends RuntimeException{
    private static final String message = "It has already user with username:";

    public UserIsAlreadyExistsWithThisUsernameException(String username) {
        super(message + username);
    }
}

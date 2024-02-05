package com.example.kamil.user.exception.customExceptions;

public class UserIsAlreadyExistsWithThisEmailException extends RuntimeException{
    private static final String message = "It has already user with email:";

    public UserIsAlreadyExistsWithThisEmailException(String email) {
        super(message + email);
    }
}

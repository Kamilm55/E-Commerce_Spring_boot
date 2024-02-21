package com.example.kamil.user.exception;

import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisEmailException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisUsernameException;
import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = UserIsNotActiveException.class)
    public ResponseEntity<?> handleUserIsNotActiveException(UserIsNotActiveException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = UserIsAlreadyExistsWithThisEmailException.class)
    public ResponseEntity<?> handleUserIsAlreadyExistsWithThisEmail(UserIsAlreadyExistsWithThisEmailException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = UserIsAlreadyExistsWithThisUsernameException.class)
    public ResponseEntity<?> handleUserIsAlreadyExistsWithThisUsername(UserIsAlreadyExistsWithThisUsernameException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleUserForbidden(AccessDeniedException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.FORBIDDEN);
    }

    // For unhandled exceptions:
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

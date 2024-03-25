package com.example.kamil.user.exception;

import com.example.kamil.user.exception.customExceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
@Slf4j
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
    @ExceptionHandler(value = PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDeniedException(PermissionDeniedException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.FORBIDDEN);
    }


    // Auth Exceptions
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleUserForbidden(AccessDeniedException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleUserIsAlreadyExistsWithThisEmail(InternalAuthenticationServiceException exception){

        log.info("hata");
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    // Validation exceptions
    // Learn:
    //  MethodArgumentNotValidException is specific to Spring and is thrown when there are validation errors during the binding of method parameters, typically in a Spring MVC controller method.
    //  It is commonly used when validating incoming request data, such as form submissions or JSON payloads,
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    // Learn:
    //  It is typically thrown when there are constraint violations during the validation of entities or objects, usually when using annotations like @NotNull, @Size, @Email, etc., on fields or properties of a Java class.
    //  This exception can occur when validating entities outside the context of Spring MVC, for example, in a JPA (Java Persistence API) environment.
    //  If you try to persist a User object with a null username using JPA, a ConstraintViolationException may be thrown
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    // For unhandled exceptions:
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception exception){
        System.out.println("For unhandled exceptions");
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.example.kamil.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 401
//        System.out.println(" works");
//        System.out.println(response.getStatus());
        if(response.getStatus() == 200)
            response.setStatus(401);

        setResponseError(/*response,*/ HttpStatus.UNAUTHORIZED, "Authentication Failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        // 403
        return setResponseError(org.springframework.http.HttpStatus.FORBIDDEN, String.format("Access Denied: %s", ex.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        // 500
//        return setResponseError(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, String.format("Internal Server Error: %s", ex.getMessage()));
//    }

    private ResponseEntity<String> setResponseError(org.springframework.http.HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status).body(errorMessage);
    }
}

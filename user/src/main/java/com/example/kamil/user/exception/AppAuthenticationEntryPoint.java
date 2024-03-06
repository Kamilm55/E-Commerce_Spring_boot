package com.example.kamil.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (response.getStatus() == 200) { // Force to 401
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication required. Please provide valid credentials.");
        } else if (response.getStatus() == 403) {
            setErrorResponse(response, HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to access this resource.");
        }


        System.out.println("AppAuthenticationEntryPoint RestControllerAdvice works");
        //  return new ResponseEntity<>(authException.getMessage(),HttpStatus.UNAUTHORIZED);
        // setResponseError(HttpStatus.UNAUTHORIZED,authException.getMessage());
    }
    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String error, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
        writer.println("{\n" +
                "  \"error\": \"" + error + "\",\n" +
                "  \"message\": \"" + message + "\"\n" +
                "}");
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        // 403
//        return setResponseError(org.springframework.http.HttpStatus.FORBIDDEN, String.format("Access Denied: %s", ex.getMessage()));
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        // 500
//        return setResponseError(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, String.format("Internal Server Error: %s", ex.getMessage()));
//    }

//    private ResponseEntity<String> setResponseError(org.springframework.http.HttpStatus status , String errorMessage) {
//        return new ResponseEntity<>(errorMessage,status);
//    }
}

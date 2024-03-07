package com.example.kamil.user.exception.customExceptions;
import org.springframework.security.access.AccessDeniedException;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String msg) {
        super(msg);
    }

    public PermissionDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}


package com.example.kamil.user.exception.customExceptions;

public class SseEmitterSendingException extends RuntimeException {
    public SseEmitterSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.example.kamil.common.response;

import org.springframework.http.HttpStatus;

public interface ResponseMessages {
    String getKey();
    String getMessage();
    HttpStatus getStatus();
}
package com.example.kamil.user.exception.customExceptions;


public class VendorRequestNotFoundException extends RuntimeException{

    public VendorRequestNotFoundException(String message) {
        super(message);
    }
}

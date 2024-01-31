package com.example.kamil.user.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class UserNotFoundException  extends RuntimeException{
    private BindingResult bindingResult;

    public UserNotFoundException(BindingResult br,String m){
        super(m);
        this.bindingResult = br;
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

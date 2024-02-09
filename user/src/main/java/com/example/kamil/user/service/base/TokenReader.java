package com.example.kamil.user.service.base;

public interface TokenReader <T>{
    T read(String token);
}

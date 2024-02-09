package com.example.kamil.user.service.base;

public interface TokenGenerator <T> {
    String generate(T obj);
}

package com.example.kamil.user.service.auth;

public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
    void removeFromBlacklist(String token);
}

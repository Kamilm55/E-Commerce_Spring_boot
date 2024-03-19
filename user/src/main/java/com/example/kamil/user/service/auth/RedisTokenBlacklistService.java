package com.example.kamil.user.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenBlacklistService implements TokenBlacklistService {
    private static final String BLACKLIST_KEY = "blacklisted_tokens";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addToBlacklist(String token) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token));
    }

    @Override
    public void removeFromBlacklist(String token) {
        redisTemplate.opsForSet().remove(BLACKLIST_KEY, token);
    }
}

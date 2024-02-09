package com.example.kamil.user.model.properties.security;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtData {
    final String publicKey;
    final String privateKey;
    final Integer accessTokenValidityTime;
//    final Integer refreshTokenValidityTime;

//    public Long getRefreshTokenValidityTime(boolean rememberMe){
//        return refreshTokenValidityTime * (rememberMe ? 30L : 1L);
//    }

}
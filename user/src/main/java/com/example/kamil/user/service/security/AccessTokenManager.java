//package com.example.kamil.user.service.security;
//
//
//import com.example.kamil.user.model.entity.User;
//import com.example.kamil.user.model.properties.security.SecurityProperties;
//import com.example.kamil.user.service.base.TokenGenerator;
//import com.example.kamil.user.service.base.TokenReader;
//import com.example.kamil.user.utils.PublicPrivateKeyUtil;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class AccessTokenManager implements TokenGenerator<User>, TokenReader<Claims> {
//    private final SecurityProperties securityProperties;
//    @Override
//    public String generate(User obj) {
//        Claims claims = Jwts.claims();
//        claims.put("email" ,obj.getEmail() );
//
//        Date now = new Date();
//        Date expDate = new Date(now.getTime() + securityProperties.getJwt().getAccessTokenValidityTime());
//
//        return Jwts.builder()
//                .addClaims(claims)
//                .setExpiration(expDate)
//                .setIssuedAt(now)
//                .setSubject(String.valueOf(obj.getId()))
//                .signWith(PublicPrivateKeyUtil.getPrivateKey() , SignatureAlgorithm.RS256)
//                .compact();
//    }
//
//    @Override
//    public Claims read(String token) {
//
//        return Jwts.parserBuilder()
//                .setSigningKey(PublicPrivateKeyUtil.getPublicKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}

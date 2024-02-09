package com.example.kamil.user.model.properties.security;

import com.example.kamil.user.model.properties.security.JwtData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties("security")
public class SecurityProperties {
    JwtData jwt;
}

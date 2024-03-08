package com.example.kamil.user.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    //Learn: Enum Constants Initialization: Enum constants are like instances of the enum type.
    // When you declare enum constants like ROLE_USER("USER"), ROLE_ADMIN("ADMIN"), etc., the constructor is invoked for each constant during enum type initialization.

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_SUPER_ADMIN("SUPER_ADMIN"),
    //ROLE_CUSTOMER("CUSTOMER"),
    ROLE_VENDOR("VENDOR");

    private final String value;
    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}

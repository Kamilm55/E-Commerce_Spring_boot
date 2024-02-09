package com.example.kamil.user.model.security;

import com.example.kamil.user.model.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class LoggedInUserDetails implements UserDetails {
    String username;
    String password;
    //Learn:
    // Used when you want to model a collection of simple (non-entity) elements associated with an entity
    // relationships such as OneToMany is used when target class is entity
    // Typically used for basic types (e.g., strings, numbers, enums) or embeddable objects (we don't need build an entity for this)
    // Creates a separate table (e.g., "authorities") to store the collection elements.
    // The owning entity (User in this case) has a foreign key reference to the collection table.
    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false) // every user ("user_id") must be reference role
    @Enumerated(EnumType.STRING)
    Set<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

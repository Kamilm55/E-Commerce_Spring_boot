package com.example.kamil.user.model.entity.security;

import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = "user") // this is important for operations in db
@ToString(exclude = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class LoggedInUserDetails implements UserDetails {

    @Id
    @GeneratedValue
    Long id;

    @OneToOne(fetch = FetchType.LAZY , mappedBy = "userDetails")
    User user;

    String phoneNumber;
    String address;
    String city;
    String country;
    String postCode;

    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false) // every user ("user_id") must be reference role
    @Enumerated(EnumType.STRING)
    Set<Role> authorities;


    public void addAuthority(Role role){
        authorities.add(role);
    }
    public void deleteAuthority(Role role){authorities.remove(role);}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

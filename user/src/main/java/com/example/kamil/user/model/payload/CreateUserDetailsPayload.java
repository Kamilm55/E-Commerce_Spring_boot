package com.example.kamil.user.model.payload;

import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserDetailsPayload {
    User user;
    Set<Role> authorities;

    String phoneNumber;
    String address;
    String city;
    String country;
    String postCode;

}

package com.example.kamil.user.model.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPayload {
    String email;
    String password;
    boolean rememberMe;
}

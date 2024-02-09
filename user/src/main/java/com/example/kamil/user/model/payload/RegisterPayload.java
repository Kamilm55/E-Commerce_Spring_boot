package com.example.kamil.user.model.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterPayload {
    String username;
    String email;

    String password;

    String firstName;
    String lastName;
}

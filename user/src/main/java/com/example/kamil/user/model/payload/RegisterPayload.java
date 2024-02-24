package com.example.kamil.user.model.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterPayload {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3 , message = "Username must be at least 3 characters long")
    String username;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please provide a valid email address")
    String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6 , message = "Password must be at least 6 characters long")
    String password;
    @NotBlank(message = "First Name is mandatory")
    @Size(min = 3 , message = "First Name must be at least 3 characters long")
    String firstName;
    @NotBlank(message = "Last Name is mandatory")
    @Size(min = 3 , message = "Last Name must be at least 3 characters long")
    String lastName;
}

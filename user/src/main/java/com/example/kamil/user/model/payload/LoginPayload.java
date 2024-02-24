package com.example.kamil.user.model.payload;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPayload {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please provide a valid email address")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6 , message = "Password must be at least 6 characters long")
    String password;

   // @NotNull(message = "RememberMe is mandatory")
    boolean rememberMe;
}

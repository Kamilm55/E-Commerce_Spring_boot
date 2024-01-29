package com.example.kamil.user.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String email;

    //private String password;
    private String firstName;
    private String lastName;
}

package com.example.kamil.user.model.payload;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String email;

    private String password;
    private String firstName;
    private String lastName;
}

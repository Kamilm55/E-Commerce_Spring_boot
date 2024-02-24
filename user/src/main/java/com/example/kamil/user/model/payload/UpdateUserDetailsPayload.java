package com.example.kamil.user.model.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDetailsPayload {
    @NotNull
    String phoneNumber;
    @NotNull
    String address;
    @NotNull
    String city;
    @NotNull
    String country;
    @NotNull
    String postCode;
}

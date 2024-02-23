package com.example.kamil.user.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggedInUserDetailsDTO {
    String phoneNumber;
    String address;
    String city;
    String country;
    String postCode;
}

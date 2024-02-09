package com.example.kamil.user.utils.converter;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;

public class UserDTOConverter {
    public static UserDTO convert(User user){

        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}

package com.example.kamil.user;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {

    public static User generateUser(String mail){
        return User.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(mail)
                .username("username")
                .isActive(true)
                .build();
    }

    public static UserDTO generateUserDto(User user){
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
    public static List<User> generateUserList(){
       return IntStream.range(0,5).mapToObj( i ->
                    User.builder()
                            .id((long) i)
                            .firstName("firstname" + i)
                            .lastName("lastname" + i)
                            .email("email" + i)
                            .username("username" + i)
                            .isActive(true)
                            .build()
                ).collect(Collectors.toList());
    }


    public static List<UserDTO> generateUserDtoList(){
        return IntStream.range(0,5).mapToObj( i ->
                UserDTO.builder()
                        .firstName("firstname" + i)
                        .lastName("lastname" + i)
                        .email("email" + i)
                        .username("username" + i)
                        .build()
        ).collect(Collectors.toList());
    }
}

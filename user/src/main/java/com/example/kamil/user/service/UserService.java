package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.payload.CreateUserRequest;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);
    void insertUser(CreateUserRequest userRequest);
    List<User> getAll();
    void deleteUser(String email);
    void deactivateUser(String email);
    void activateUser(String email);

}

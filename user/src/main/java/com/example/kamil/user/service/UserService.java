package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.payload.CreateUserRequest;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);
    void insertUser(CreateUserRequest userRequest);
    List<UserDTO> getAll();
    List<UserDTO> getActiveUsers();
    void deleteUser(String email);
    void deactivateUser(String email);
    void activateUser(String email);
    UserDTO updateUser(String email , CreateUserRequest userRequest);

}

package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.payload.UserRequest;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO getUserByEmail(String email);
    UserDTO insertUser(UserRequest userRequest);
    UserDTO updateUser(String email , UserRequest userRequest);
    List<UserDTO> getActiveUsers();
    void deleteUser(String email);
    void deactivateUser(String email);
    void activateUser(String email);

}

package com.example.kamil.user.service;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.payload.RegisterPayload;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    List<UserDTO> getActiveUsers();
    UserDTO getUserByEmail(String email);
    User getUserByEmailForUserDetails(String email); // for loadByUsername
    UserDTO insertUser(RegisterPayload userRequest);
    UserDTO updateUser(String email , RegisterPayload userRequest);
    void deactivateUser(String email);
    void activateUser(String email);
    void deleteUser(String email);

    User findUserByEmail(String email);

    User addAdminRole (User user);
}

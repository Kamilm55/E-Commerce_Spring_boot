package com.example.kamil.user.service;

import com.example.kamil.user.entity.User;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);
    void insertUser(User user);
    List<User> getAll();
    boolean deleteUser(String email);
    void deactivateUser();

}

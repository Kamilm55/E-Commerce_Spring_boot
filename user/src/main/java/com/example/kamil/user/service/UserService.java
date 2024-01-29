package com.example.kamil.user.service;

public interface UserService {
    User getUserByEmail(String email);
    void insertUser(User user);
    List<User> getAll();
    boolean deleteUser();
    void deactivateUser();

}

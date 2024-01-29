package com.example.kamil.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {

    }

    @Override
    public void insertUser(User user) {

    }

    @Override
    public List<User> getAll() {

    }

    @Override
    public boolean deleteUser() {

    }

    @Override
    public void deactivateUser() {

    }
}

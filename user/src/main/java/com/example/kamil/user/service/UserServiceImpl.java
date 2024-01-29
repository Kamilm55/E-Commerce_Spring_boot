package com.example.kamil.user.service;

import com.example.kamil.user.entity.User;
import com.example.kamil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
      return findUserByEmail(email);
     }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(String email) {
        User user = findUserByEmail(email);

        userRepository.deleteById(user.getId());
        return  true;
    }

    @Override
    public void deactivateUser() {
    /// deactivate user
    }

    // Util methods
    private User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found with this email: " + email));
    }
}

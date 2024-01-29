package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.payload.CreateUserRequest;
import com.example.kamil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserByEmail(String email) {

        User user = findUserByEmail(email);
        //refactor
        UserDTO userDto = UserDTO.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
      return userDto;
     }

    @Override
    public void insertUser(CreateUserRequest userRequest) {
        //refactor
        User user = User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .build();

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

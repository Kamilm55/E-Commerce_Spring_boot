package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.payload.CreateUserRequest;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = findUserByEmail(email);

        return UserDTOConverter.convert(user);
     }

    @Override
    public void insertUser(CreateUserRequest userRequest) {
        User user = populateUser(userRequest);

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserDTOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.isActive())
                .map(user -> UserDTOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {
        User user = findUserByEmail(email);

        userRepository.deleteById(user.getId());
    }

    @Override
    public void deactivateUser(String email) {
        changeStatusOfUser(email,false);
    }

    @Override
    public void activateUser(String email) {
        changeStatusOfUser(email,true);
    }

    @Override
    public UserDTO updateUser(String email, CreateUserRequest userRequest) {
        User user = findUserByEmail(email);

        if(!user.isActive()){
            throw new UserIsNotActiveException();
        }

        updateUserData(user,userRequest); // not save only set

        User updatedUser = saveAndRetrieveUser(user);

        return UserDTOConverter.convert(updatedUser);
    }

    // Util methods

    private User populateUser(CreateUserRequest userRequest){
        return User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .isActive(true)
                .build();
    }
    private void updateUserData(User user, CreateUserRequest userRequest) {
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
    }
    private User saveAndRetrieveUser(User user) {
        userRepository.save(user);
        // Fetch the updated user from the database to ensure data consistency
        return findUserByEmail(user.getEmail());
    }
    private void changeStatusOfUser(String email,boolean status){
        User user = findUserByEmail(email);
        user.setActive(status);
        userRepository.save(user);
    }
    private User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found with this email: " + email));
    }

}

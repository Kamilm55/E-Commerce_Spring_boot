package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.payload.UserRequest;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = findUserByEmail(email);

        return UserDTOConverter.convert(user);
     }

    @Override
    public UserDTO insertUser(UserRequest userRequest) {
        //refactorThis:

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("It has already user with email:" + userRequest.getEmail());
        }
        else if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new RuntimeException("It has already user with username:" + userRequest.getUsername());
        }


        ///
        User user = populateUser(userRequest);

        return UserDTOConverter.convert( userRepository.save(user));
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
                .filter(user -> user.getIsActive())
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
    public UserDTO updateUser(String email, UserRequest userRequest) {
        User user = findUserByEmail(email);

        if(!user.getIsActive()){
            log.warn(String.format("User with email: %s is not active!",email));
            throw new UserIsNotActiveException();
        }

        updateUserData(user,userRequest); // not save only set

        User updatedUser = userRepository.save(user);;

        return UserDTOConverter.convert(updatedUser);
    }

    // Util methods

    private User populateUser(UserRequest userRequest){
        return User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .isActive(false)
                .build();
    }
    private void updateUserData(User user, UserRequest userRequest) {
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
    }
    private void changeStatusOfUser(String email,boolean status){
        User user = findUserByEmail(email);
        user.setIsActive(status);
        userRepository.save(user);
    }
    private User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found with this email: " + email));
    }

}

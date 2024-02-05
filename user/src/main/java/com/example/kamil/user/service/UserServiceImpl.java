package com.example.kamil.user.service;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisEmailException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisUsernameException;
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
       validateUniquenessOfEmailAndUsername(userRequest);

        User user = populateUser(userRequest);

        return UserDTOConverter.convert( userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(String email, UserRequest userRequest) {
        User userFromDB = findUserByEmail(email);

        if(!userFromDB.getIsActive()){
            log.warn(String.format("User with email: %s is not active!",email));
            throw new UserIsNotActiveException();
        }

        validateUniquenessOfEmailAndUsername(userRequest);

        User updatedUserData = updateUserData(userFromDB, userRequest);// not save only set

        User updatedUser = userRepository.save(updatedUserData); // save

        return UserDTOConverter.convert(updatedUser);
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


    // Util methods

    private void validateUniquenessOfEmailAndUsername(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String username = userRequest.getUsername();

        // Learn:
        //  For update method we must check existing user
        //  It can be unmodified fields but it throws exception
        //  existingUser == null -> insert method
        //  !existingUser.getEmail().equals(email) -> update method

        // Retrieve the existing user from the database
        User existingUser = userRepository.findByEmail(email).orElse(null);

        //todo: for update it throw error 500 but it must throw UserIsAlreadyExistsWithThisEmail


        log.warn("before email check ");
        System.out.println();

        log.info("email: " + email);
        if(existingUser != null){
            log.info("existingUser.getEmail(): " + existingUser.getEmail());
             log.info("email is equal to existingUser.getEmail()"+existingUser.getEmail().equals(email));
        }

                // Check for email uniqueness only if the email is being changed
        if (existingUser == null || !existingUser.getEmail().equals(email)) {
            log.warn("email inside");
            if (userRepository.existsByEmail(email)) {
                throw new UserIsAlreadyExistsWithThisEmailException(email);
            }
        }

        // Check for username uniqueness only if the username is being changed
        if (existingUser == null || !existingUser.getUsername().equals(username)) {
            if (userRepository.existsByUsername(username)) {
                throw new UserIsAlreadyExistsWithThisUsernameException(username);
            }
        }
    }
    private User populateUser(UserRequest userRequest){
        return User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .isActive(false)
                .build();
    }
    private User updateUserData(User user, UserRequest userRequest) {
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return user;
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

package com.example.kamil.user.service;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisEmailException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisUsernameException;
import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = findUserByEmail(email);

        return UserDTOConverter.convert(user);
     }

    @Override
    public User getUserByEmailForUserDetails(String email) {
      return findUserByEmail(email);
    }

    @Override
    public UserDTO insertUser(RegisterPayload userRequest) {
        validateUniquenessOfEmailAndUsername(userRequest);

        User user = populateUser(userRequest);

        return UserDTOConverter.convert( userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(String email, RegisterPayload userRequest) {
        User userFromDB = findUserByEmail(email);

        if(!userFromDB.getIsActive()){
            log.warn(String.format("User with email: %s is not active!",email));
            throw new UserIsNotActiveException();
        }

        validateForUpdate(userFromDB,userRequest);

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
    private void validateForUpdate(User currentUser, RegisterPayload userRequest) {
        String emailFromRequest = userRequest.getEmail();
        String usernameFromRequest = userRequest.getUsername();

        if(!currentUser.getEmail().equals(emailFromRequest)){
            ifExistsByEmailThrowException(emailFromRequest);
        }
        else if (!currentUser.getUsername().equals(usernameFromRequest)) {
            ifExistsByUsernameThrowException(usernameFromRequest);
        }

    }

    private void validateUniquenessOfEmailAndUsername(RegisterPayload userRequest) {
       ifExistsByEmailThrowException(userRequest.getEmail());
       ifExistsByUsernameThrowException(userRequest.getUsername());
    }
    private void ifExistsByUsernameThrowException(String username) {
        if(userRepository.existsByUsername(username))
            throw new UserIsAlreadyExistsWithThisUsernameException(username);
    }

    private void ifExistsByEmailThrowException(String email) {
        if(userRepository.existsByEmail(email))
            throw new UserIsAlreadyExistsWithThisEmailException(email);
    }
    private User populateUser(RegisterPayload userRequest){
        return User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .isActive(false)
                .build();
    }
    private User updateUserData(User user, RegisterPayload userRequest) {
        // Email and username must be unique
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
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

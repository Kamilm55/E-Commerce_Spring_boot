package com.example.kamil.user.service;

import com.example.kamil.user.TestSupport;
import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisEmailException;
import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisUsernameException;
import com.example.kamil.user.model.payload.UserRequest;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
public class UserServiceTest extends TestSupport {
    //Learn:
    // In JUnit and many other unit testing frameworks, the common structure of a test is often based on the Arrange-Act-Assert (AAA) pattern.

    // Learn:
    //  assertEquals(expectedValue, actualValue) actualValue from service
    private UserRepository userRepository;
    private UserDTOConverter userDTOConverter;

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    // TODO: check that it encodes actually password or not

    //1.Arrange (Setup):
    @BeforeEach
    public void setUp() {
        // Arrange: Initialization and setup

        // Create a mock objects for using in when and verify methods
        //userDTOConverter = mock(UserDTOConverter.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserServiceImpl(userRepository,passwordEncoder);
    }

    @Test
    public void testGetAll_itShouldReturnUserDtoList(){
        //todo: how to mock static method in junit 5 , mockito

        //  1.Preparation
        List<User> users = generateUserList();
        List<UserDTO> userDTOList = generateUserDtoList(users);

        //User user = generateUser();

        // Define the behavior of the mock
        // 2.Condition
        when(userRepository.findAll()).thenReturn(users);
        //when(UserDTOConverter.convert(user)).thenReturn(generateUserDto(user));

        // 3.Service call
        List<UserDTO> result = userService.getAll();

        // 4.Equality
        // Assert: Validate the results
        //    assertEquals(expectedValue, actualValue) actualValue from service
        assertEquals(userDTOList , result);

        // verifications
        verify(userRepository).findAll();
        //verifyStatic(userDTOConverter) //todo: verify static method in junit 5 , mockito

    }

    @Test
    public void testGetActiveUsers_itShouldReturnUserDtoList(){
        //todo: how to mock static method in junit 5 , mockito

        //  1.Preparation
        List<User> users = generateUserList();

        List<User> userListOnlyActive = users.stream()
                .filter(User::getIsActive)
                .collect(Collectors.toList());

        List<UserDTO> userDTOListOnlyActive = generateUserDtoList(userListOnlyActive);

        //User user = generateUser();

        // Define the behavior of the mock
        // 2.Condition
        when(userRepository.findAll()).thenReturn(users);

        // 3.Service call
        List<UserDTO> result = userService.getActiveUsers();

        // 4.Equality
        // Assert: Validate the results
        //    assertEquals(expectedValue, actualValue) actualValue from service
        assertEquals(userDTOListOnlyActive , result);

        // verifications
        verify(userRepository).findAll();

    }
    @Test
    public void testGetUserByEmail_whenUserMailExists_itShouldReturnUserDTO(){
        // A-A-A Arrange-Act-Assert

        // Arrange:
        String mail = "user@gmail.com";
        User user = generateUser(mail);
        UserDTO userDTO = generateUserDto(user);

        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));

        // Act:
        UserDTO result = userService.getUserByEmail(mail);

        // Assert:
        assertEquals(userDTO,result);

        verify(userRepository).findByEmail(mail);
    }

    @Test
    public void testGetUserByEmail_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        User user = generateUser(mail);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Assert:
        assertThrows(UserNotFoundException.class , ()->{
            // Act:
            userService.getUserByEmail(mail);
        });

        verify(userRepository).findByEmail(mail);
    }


    @Test
    public void testInsertUser_itShouldReturnCreatedUserDto(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").build();
       // before save
        User user = User.builder().email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").isActive(false).build();
       // after save
        User savedUser = User.builder()
                .id(1L)
                .email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").isActive(false).build();

        UserDTO userDTO = generateUserDto(user);

        // condition:
        when(userRepository.save(user)).thenReturn(savedUser);

        when(userRepository.existsByEmail(mail)).thenReturn(false);
        when(userRepository.existsByUsername(mail)).thenReturn(false);

        // Act:
        UserDTO result = userService.insertUser(userRequest);

        // Assert:
        assertEquals(userDTO,result);

        verify(userRepository).save(user);
    }

    @Test
    public void testInsertUser_WhenUserIsAlreadyExistsWithEmail_itShouldThrowUserIsAlreadyExistsWithThisEmailException(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("first").lastName("l").username("user").build();

        // condition:
        when(userRepository.existsByEmail(mail)).thenReturn(true);

        assertThrows(UserIsAlreadyExistsWithThisEmailException.class , () ->{
            userService.insertUser(userRequest);
        });

        verify(userRepository).existsByEmail(mail);
    }
    @Test
    public void testInsertUser_WhenUserIsAlreadyExistsWithUsername_itShouldThrowUserIsAlreadyExistsWithThisEmailException(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("first").lastName("l").username("user").build();

        // condition:
        when(userRepository.existsByEmail(mail)).thenReturn(false);
        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(true);

        assertThrows(UserIsAlreadyExistsWithThisUsernameException.class , () ->{
            userService.insertUser(userRequest);
        });
        verify(userRepository).existsByEmail(mail);
        verify(userRepository).existsByUsername(userRequest.getUsername());
    }

    // Update method tests
    @Test
    public void testUpdateUser_whenUserMailDoesExistAndIsActive_itShouldReturnUpdatedUserDto(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").build();
        // before save
        User user = User.builder().email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").isActive(true).build();
//
//        User updateUser = User.builder()
//                .id(1L)
//                .email(mail).firstName("first").lastName("l").username("user").isActive(true).build();
        // after save
        User savedUser = User.builder()
                .id(1L)
                .email(mail)
                .password(passwordEncoder.encode("pass"))
                .firstName("first").lastName("l").username("user").isActive(true).build();

        UserDTO userDTO = generateUserDto(user);

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act:
        UserDTO result = userService.updateUser(mail,userRequest);

        // Assert:
        assertEquals(userDTO,result);

        // Verify that findByEmail is called exactly two times with the specified email parameter
        verify(userRepository).findByEmail(mail);
        verify(userRepository).save(user);
    }
    @Test
    public void testUpdateUser_whenUserMailDoesNotExistAndIsActive_itShouldThrowUserNotFoundException(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("first").lastName("l").username("user").build();
        // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(true).build();
//
//        User updateUser = User.builder()
//                .id(1L)
//                .email(mail).firstName("first").lastName("l").username("user").isActive(true).build();
        // after save
        User savedUser = User.builder()
                .id(1L)
                .email(mail).firstName("first").lastName("l").username("user").isActive(true).build();

       // UserDTO userDTO = generateUserDto(user);

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());
       // when(userRepository.save(user)).thenReturn(savedUser);

        // Assert:
        assertThrows(UserNotFoundException.class , () -> {
            // Act:
            userService.updateUser(mail,userRequest);
        });


        verify(userRepository).findByEmail(mail);
        verifyNoMoreInteractions(userRepository);
      //  verify(userRepository).save(user);
    }

    @Test
    public void testUpdateUser_whenUserMailExistsButUserIsNotActive_itShouldThrowUserIsNotActiveException(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("first").lastName("l").username("user").build();
        // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(false).build();
//
//        User updateUser = User.builder()
//                .id(1L)
//                .email(mail).firstName("first").lastName("l").username("user").isActive(false).build();
        // after save
        User savedUser = User.builder()
                .id(1L)
                .email(mail).firstName("first").lastName("l").username("user").isActive(false).build();

        // UserDTO userDTO = generateUserDto(user);

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(savedUser);

        // Assert:
        assertThrows(UserIsNotActiveException.class , () -> {
            // Act:
            userService.updateUser(mail,userRequest);
        });


        verify(userRepository).findByEmail(mail);
        verifyNoMoreInteractions(userRepository);
        // verify(userRepository).save(user);
    }

    @Test
    public void testUpdateUser_whenUserMailExistsAndUserIsActiveButOtherUserIsAlreadyExistsWithEmail_itShouldThrowUserIsAlreadyExistsWithThisEmailException(){
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("reqF").lastName("reqL").username("reqUser").build();

        User savedUser = User.builder()
                .id(1L)
                .email("different mail").firstName("first").lastName("l").username("user").isActive(true).build();



        // condition:

        // Set up the mock to return Optional.of(user) for the first invocation
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(savedUser));
        when(userRepository.existsByEmail(mail)).thenReturn(true);
       // when(userRepository.save(savedUser)).thenReturn(updatedUser);

        // Assert:
        assertThrows(UserIsAlreadyExistsWithThisEmailException.class , () -> {
            // Act:
            userService.updateUser(mail,userRequest);
        });


        verify(userRepository ).findByEmail(mail);
        verify(userRepository).existsByEmail(mail);
    }
    @Test
    public void testUpdateUser_whenUserMailExistsAndUserIsActiveButOtherUserIsAlreadyExistsWithUsername_itShouldThrowUserIsAlreadyExistsWithThisUsernameException(){
        // Arrange:
        String mail = "user@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("reqF").lastName("reqL").username("reqUser").build();

        User savedUser = User.builder()
                .id(1L)
                .email(mail).firstName("first").lastName("l").username("diff user").isActive(true).build();


        // condition:

        // Set up the mock to return Optional.of(user) for the first invocation
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(savedUser));
        when(userRepository.existsByEmail(mail)).thenReturn(false);
        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(true);

        // Assert:
        assertThrows(UserIsAlreadyExistsWithThisUsernameException.class , () -> {
            // Act:
            userService.updateUser(mail,userRequest);
        });


        verify(userRepository).findByEmail(mail);
        verify(userRepository).existsByUsername(userRequest.getUsername());
    }

    // Deactivate method tests 2 cases
    @Test
    public void testDeactivateUser_whenUserMailExists_itShouldUpdateUserByActive(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(true).build();

        // after save : it's status should change to false
        User savedUser = User.builder()
                .email(mail).firstName("first").lastName("l").username("user").isActive(false).build();


        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));

        log.info("user:" + user);
        log.info("Saved user:" + savedUser);

        log.info("****");
        // Act:
         userService.deactivateUser(mail);

        log.info("user:" + user);
        log.info("Saved user:" + savedUser);

        log.info("Saved user equals user? " + user.equals(savedUser));

         // There is no assertion for void methods , we must verify
        verify(userRepository).findByEmail(mail);
        verify(userRepository).save(savedUser); // (not user) -> (after deactivate method with the help of setActive() , user equals savedUser , therefore it can be user)

        // user with isActive of true
//        User userActive = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(true).build();
//        verify(userRepository).save(userActive);  // Learn: Test fails in this case

    }
    @Test
    public void testDeactivateUser_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        // Arrange:
        String mail = "user@gmail.com";

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class , () -> {
            // Act:
            userService.deactivateUser(mail);
        });

        verify(userRepository).findByEmail(mail);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testActivateUser_whenUserMailExists_itShouldUpdateUserByActive(){
        // A-A-A Arrange-Act-Assert
        // Arrange:
        String mail = "user@gmail.com";
        // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(false).build();

        // after save : it's status should change to false
        User savedUser = User.builder()
                .email(mail).firstName("first").lastName("l").username("user").isActive(true).build();


        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));

        log.info("user:" + user);
        log.info("Saved user:" + savedUser);

        log.info("****");
        // Act:
        userService.activateUser(mail);

        log.info("user:" + user);
        log.info("Saved user:" + savedUser);

        log.info("Saved user equals user? " + user.equals(savedUser));

        // There is no assertion for void methods , we must verify
        verify(userRepository).findByEmail(mail);
        verify(userRepository).save(savedUser); // (not user) -> (after deactivate method with the help of setActive() , user equals savedUser , therefore it can be user)

        // user with isActive of false
//        User userActive = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(false).build();
//        verify(userRepository).save(userActive);  // Learn: Test fails in this case

    }

    @Test
    public void testActivateUser_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        // Arrange:
        String mail = "user@gmail.com";

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class , () -> {
            // Act:
            userService.activateUser(mail);
        });

        verify(userRepository).findByEmail(mail);
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void testDeleteUser_whenUserMailExists_itShouldDeleteUser(){
        // Arrange:
        String mail = "user@gmail.com";
        // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(false).build();

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));

        // Act:
        userService.deleteUser(mail);

        // There is no assertion for void methods , we must verify
        verify(userRepository).findByEmail(mail);
        verify(userRepository).deleteById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDeleteUser_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        // Arrange:
        String mail = "user@gmail.com";

        // condition:
        when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class , () -> {
            // Act:
            userService.deleteUser(mail);
        });

        verify(userRepository).findByEmail(mail);
        verifyNoMoreInteractions(userRepository);
    }

}

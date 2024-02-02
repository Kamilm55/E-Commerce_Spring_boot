package com.example.kamil.user.service;

import com.example.kamil.user.TestSupport;
import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.payload.UserRequest;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest extends TestSupport {
    //Learn:
    // In JUnit and many other unit testing frameworks, the common structure of a test is often based on the Arrange-Act-Assert (AAA) pattern.

    private UserRepository userRepository;
    private UserDTOConverter userDTOConverter;

    private UserService userService;

    //1.Arrange (Setup):
    @BeforeEach
    public void setUp() {
        // Arrange: Initialization and setup

        // Create a mock objects for using in when and verify methods
        //userDTOConverter = mock(UserDTOConverter.class);
        userRepository = mock(UserRepository.class);

        userService = new UserServiceImpl(userRepository);
    }
    // Learn:  assertEquals(expectedValue, actualValue) actualValue from service

    @Test
    public void testGetAll_itShouldReturnUserDtoList(){
        //todo: how to mock static method in junit 5 , mockito

        //  1.Preparation
        List<User> users = generateUserList();
        List<UserDTO> userDTOList = generateUserDtoList();

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
        UserRequest userRequest = UserRequest.builder().email(mail).firstName("first").lastName("l").username("user").build();
       // before save
        User user = User.builder().email(mail).firstName("first").lastName("l").username("user").isActive(false).build();
       // after save
        User savedUser = User.builder()
                .id(1L)
                .email(mail).firstName("first").lastName("l").username("user").isActive(false).build();

        UserDTO userDTO = generateUserDto(user);

        // condition:
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act:
        UserDTO result = userService.insertUser(userRequest);

        // Assert:
        assertEquals(userDTO,result);

        verify(userRepository).save(user);
    }

}

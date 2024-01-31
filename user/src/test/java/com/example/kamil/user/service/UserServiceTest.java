package com.example.kamil.user.service;

import com.example.kamil.user.TestSupport;
import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void testGetAllUsers_itShouldReturnUserDtoList(){
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
}

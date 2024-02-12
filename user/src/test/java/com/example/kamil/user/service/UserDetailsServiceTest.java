//package com.example.kamil.user.service;
//
//import com.example.kamil.user.TestSupport;
//import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
//import com.example.kamil.user.model.entity.User;
//import com.example.kamil.user.model.enums.Role;
//import com.example.kamil.user.model.security.LoggedInUserDetails;
//import com.example.kamil.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class UserDetailsServiceTest extends TestSupport {
//    private UserRepository userRepository;
//    private UserService userService;
//    private UserDetailsService userDetailsService;
//
//
//    @BeforeEach
//    public void setUp() {
//        // Arrange: Initialization and setup
//
//        // Create a mock objects for using in when and verify methods
//        userRepository = mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
//
//        userService = new UserServiceImpl(userRepository, passwordEncoder);
//
//        // Initialize userDetailsService with a new instance of your UserDetailsService implementation
//        userDetailsService = new UserDetailsServiceImpl(userService);
//    }
////
////    @Test
////    public void testLoadUserByUsername_itShouldReturnUserLoggedInUserDetails(){
////        //todo: how to mock static method in junit 5 , mockito
////
////        //  1.Preparation
////        String email = "email@gmail.com";
////        User user = generateUser(email);
////        LoggedInUserDetails loggedInUserDetails = new LoggedInUserDetails(email,user.getPassword(), Set.of(Role.ROLE_USER));
////
////        // Define the behavior of the mock
////        // 2.Condition
////        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
////
////        // 3.Service call
////        UserDetails result = userDetailsService.loadUserByUsername(email);
////
////        // 4.Equality
////        // Assert: Validate the results
////        assertEquals(loggedInUserDetails , result);
////
////        // verifications
////        verify(userRepository).findByEmail(email);
////    }
//
////    @Test
////    public void testLoadUserByUsername_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
////        //todo: how to mock static method in junit 5 , mockito
////
////        //  1.Preparation
////        String email = "email@gmail.com";
////        User user = generateUser(email);
////        LoggedInUserDetails loggedInUserDetails = new LoggedInUserDetails(email,user.getPassword(), Set.of(Role.ROLE_USER));
////
////        // Define the behavior of the mock
////        // 2.Condition
////        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
////
////
////        // 4.Equality
////        // Assert: Validate the results
////        assertThrows(UserNotFoundException.class, () -> {
////            // 3.Service call
////            userDetailsService.loadUserByUsername(email);
////        });
////
////        // verifications
////        verify(userRepository).findByEmail(email);
////    }
//
//
//}

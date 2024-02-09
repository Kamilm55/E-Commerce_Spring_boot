package com.example.kamil.user.service;

import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.enums.Role;
import com.example.kamil.user.model.security.LoggedInUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.getUserByEmailForUserDetails(email); // fetch details of user from db

        log.warn("USER INFO:");
        System.out.println(user);

        //refactorThis: role can be change
        if(user!=null){
            return new LoggedInUserDetails(user.getEmail(),user.getPassword(),Set.of(Role.ROLE_USER));
        }else {
            throw new UsernameNotFoundException("There is no authenticated user with this email");
        }

    }
}

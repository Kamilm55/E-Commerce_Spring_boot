package com.example.kamil.user.service.impl;

import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.Role;
import com.example.kamil.user.model.payload.UserDetailsPayload;
import com.example.kamil.user.repository.LoggedInUserDetailsRepository;
import com.example.kamil.user.service.LoggedInUserDetailsService;
import com.example.kamil.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggedInUserDetailsServiceImpl implements LoggedInUserDetailsService {
    private final LoggedInUserDetailsRepository userDetailsRepository;
    private final UserService userService;

    @Override
    public void insertUserDetails(UserDetailsPayload payload) {
        LoggedInUserDetails userDetails = LoggedInUserDetails.builder()
                .user(payload.getUser())
                .authorities(payload.getAuthorities())
                .build();

        userDetailsRepository.save(userDetails);
    }

    @Override
    @Transactional // It makes active hibernate session , we cannot get lazy init obj without this
    public LoggedInUserDetails getUserDetails(User user) {
        User userFromDb = userService.findUserByEmail(user.getEmail());

//        System.out.println(userFromDb.getUserDetails());
        //todo: why when i print it works otherwise not?
        return userFromDb.getUserDetails();
    }

}

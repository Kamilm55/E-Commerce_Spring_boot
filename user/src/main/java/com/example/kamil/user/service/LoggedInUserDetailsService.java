package com.example.kamil.user.service;


import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.payload.UserDetailsPayload;

public interface LoggedInUserDetailsService {
    void insertUserDetails(UserDetailsPayload payload);
    LoggedInUserDetails getUserDetails(User user);


}

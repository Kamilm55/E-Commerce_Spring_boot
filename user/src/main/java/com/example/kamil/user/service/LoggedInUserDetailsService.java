package com.example.kamil.user.service;


import com.example.kamil.user.model.dto.LoggedInUserDetailsDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.payload.UpdateUserDetailsPayload;
import com.example.kamil.user.model.payload.CreateUserDetailsPayload;

import java.util.List;

public interface LoggedInUserDetailsService {
  //  void insertUserDetails(CreateUserDetailsPayload payload);
    LoggedInUserDetails getUserDetailsWithUserPayload(User user);
    LoggedInUserDetailsDTO getUserDetailsByEmail(String email);
    LoggedInUserDetailsDTO updateUserUserDetails(String email, UpdateUserDetailsPayload updateUserDetailsPayload);
    void addAdminRole(String email);
    void addSuperAdminRole(String email);
    void deleteAdminRole(String email);
    void addVendorRole(String email);
    void deleteVendorRole(String email);
    void requestForVendorRole(String email);//user role
    void approveVendorRequest(String email);//admin role
    void rejectVendorRequest(String email);//admin role



    // List<LoggedInUserDetailsDTO> getAll();
}

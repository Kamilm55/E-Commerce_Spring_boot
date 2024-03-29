package com.example.kamil.user.service;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.dto.VendorRequestDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.payload.RegisterPayload;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    List<UserDTO> getActiveUsers();
    UserDTO getUserByEmail(String email);
    User getUserByEmailForUserDetails(String email); // for loadByUsername
    UserDTO insertUser(RegisterPayload userRequest);
    UserDTO updateUser(String email , RegisterPayload userRequest);
    void deactivateUser(String email);
    void activateUser(String email);
    void deleteUser(String email);

    User findUserByEmail(String email);

    void checkUserIsSameWithAuthenticatedUser(String email,String exMessage);

    LoggedInUserDetails getAuthenticatedUser();

    void sendRequestForVendorRole(String email);

    SseEmitter listenVendorRequestEmitter();

    VendorRequestDTO readVendorRequest(Long vendorReqId);

    VendorRequestDTO approveVendorRequest(Long vendorReqId);

    VendorRequestDTO rejectVendorRequest(Long vendorReqId);

    SseEmitter listenUserNotificationMessage(String email);

    UserNotificationDTO readUserNotification(Long notificationId);
}

package com.example.kamil.user.service;

import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.entity.UserNotification;

import java.util.List;

public interface UserNotificationService {
    UserNotificationDTO save(UserNotification userNotification);

    List<UserNotificationDTO> findUnreadMessages();

    List<UserNotificationDTO> findUnreadMessagesByUserEmail(String email);

    UserNotification findById(Long notificationId);
}

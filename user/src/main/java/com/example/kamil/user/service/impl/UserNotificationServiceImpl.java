package com.example.kamil.user.service.impl;

import com.example.kamil.user.exception.customExceptions.UserNotificationNotFoundException;
import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.entity.UserNotification;
import com.example.kamil.user.repository.UserNotificationRepository;
import com.example.kamil.user.service.UserNotificationService;
import com.example.kamil.user.utils.mapper.UserNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.kamil.user.model.enums.UserNotificationStatus.UNREAD;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {
    private final UserNotificationRepository userNotificationRepository;
    @Override
    public UserNotificationDTO save(UserNotification userNotification) {
        UserNotification savedUserNotification = userNotificationRepository.save(userNotification);
        return UserNotificationDTO.builder()
                .id(savedUserNotification.getId())
                .email(savedUserNotification.getUserDetails().getEmail())
                .status(savedUserNotification.getStatus())
                .content(savedUserNotification.getContent())
                .createdAt(savedUserNotification.getCreatedAt())
                .build();
    }

    @Override
    public List<UserNotificationDTO> findUnreadMessages() {
        return userNotificationRepository.findByStatus(UNREAD).stream().map( not -> UserNotificationDTO.builder()
                .id(not.getId())
                .email(not.getUserDetails().getEmail())
                .status(not.getStatus())
                .content(not.getContent())
                .createdAt(not.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<UserNotificationDTO> findUnreadMessagesByUserEmail(String email) {
        return userNotificationRepository.findByStatusAndUserDetails_User_Email(UNREAD,email).stream().map( not -> UserNotificationDTO.builder()
                .id(not.getId())
                .email(not.getUserDetails().getEmail())
                .status(not.getStatus())
                .content(not.getContent())
                .createdAt(not.getCreatedAt())
                .build()).collect(Collectors.toList());

    }

    @Override
    public UserNotification findById(Long notificationId) {
        return userNotificationRepository.findById(notificationId).orElseThrow(() -> new UserNotificationNotFoundException("User notification not found with id: " + notificationId));
    }
}

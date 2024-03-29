package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.UserNotification;
import com.example.kamil.user.model.enums.UserNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification,Long> {
    List<UserNotification> findByStatus(UserNotificationStatus status);
    List<UserNotification> findByStatusAndUserDetails_User_Email(UserNotificationStatus status , String email);
}

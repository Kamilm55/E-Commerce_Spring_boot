package com.example.kamil.user.model.dto;


import com.example.kamil.user.model.enums.UserNotificationStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserNotificationDTO {
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private UserNotificationStatus status;
    @Column(nullable = false)
    private String content;
}

package com.example.kamil.user.model.entity;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.UserNotificationStatus;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(exclude = "userDetails") // this is important for operations in db
@ToString(exclude = "userDetails")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_notification")
@Data
public class UserNotification {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
//    @Column(nullable = false)
//    private LocalDateTime respondedAt;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "userDetails_id" , referencedColumnName = "id",nullable = false)
    @JsonIgnore // Ignore during serialization to break the loop
    private LoggedInUserDetails userDetails;

    @Column(nullable = false)
    private UserNotificationStatus status;
    @Column(nullable = false)
    private String content;
}

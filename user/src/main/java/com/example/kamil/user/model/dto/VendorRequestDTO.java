package com.example.kamil.user.model.dto;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorRequestDTO {
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
    @Column(nullable = false)
    private VendorRoleStatus vendorRoleStatus;
    private String email;
}

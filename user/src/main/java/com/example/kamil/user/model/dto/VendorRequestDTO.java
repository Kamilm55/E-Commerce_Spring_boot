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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorRequestDTO {
    @Column(nullable = false)
    private LocalDate createdAt;
    private LocalDate respondedAt;
    @Column(nullable = false)
    private VendorRoleStatus vendorRoleStatus;
    private String email;
}

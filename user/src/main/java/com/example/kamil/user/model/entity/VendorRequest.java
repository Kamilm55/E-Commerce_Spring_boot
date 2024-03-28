package com.example.kamil.user.model.entity;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(exclude = "userDetails") // this is important for operations in db
@ToString(exclude = "userDetails")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendor_request")
@Data
public class VendorRequest {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime respondedAt;// default createdAt = respondedAt

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "userDetails_id" , referencedColumnName = "id",nullable = false)
    @JsonIgnore // Ignore during serialization to break the loop
    private LoggedInUserDetails userDetails;

    @Column(nullable = false)
    private VendorRoleStatus vendorRoleStatus;
}

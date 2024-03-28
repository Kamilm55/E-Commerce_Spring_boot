package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRequestRepository extends JpaRepository<VendorRequest,Long> {
    List<VendorRequest> findByVendorRoleStatus(VendorRoleStatus requested);

    boolean existsVendorRequestByUserDetails_User_Email(String email);
    VendorRequest findVendorRequestByUserDetails_User_Email(String email);
    // Custom method to find VendorRequests by user email and createdAt near now
    Optional<VendorRequest> findFirstByUserDetails_User_EmailOrderByCreatedAtDesc(String email);
}

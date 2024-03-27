package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRequestRepository extends JpaRepository<VendorRequest,Long> {
    List<VendorRequest> findByVendorRoleStatus(VendorRoleStatus requested);
}

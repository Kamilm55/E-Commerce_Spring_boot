package com.example.kamil.user.service.impl;

import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import com.example.kamil.user.repository.VendorRequestRepository;
import com.example.kamil.user.service.VendorRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorRequestServiceImpl implements VendorRequestService {
    private final VendorRequestRepository vendorRequestRepository;


    @Override
    public void insertVendorRequest(VendorRequest vendorRequest) {
        vendorRequestRepository.save(vendorRequest);
    }

    @Override
    public List<VendorRequest> findUnreadMessages() {
        return vendorRequestRepository.findByVendorRoleStatus(VendorRoleStatus.REQUESTED);
    }
}

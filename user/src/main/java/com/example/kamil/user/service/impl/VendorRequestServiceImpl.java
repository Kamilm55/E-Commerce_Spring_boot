package com.example.kamil.user.service.impl;

import com.example.kamil.user.exception.customExceptions.VendorRequestNotFoundException;
import com.example.kamil.user.model.dto.VendorRequestDTO;
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
    public List<VendorRequest> findUnreadMessages() {
        return vendorRequestRepository.findByVendorRoleStatus(VendorRoleStatus.REQUESTED);
    }

    @Override
    public VendorRequest getById(Long id) {
        return vendorRequestRepository.findById(id).orElseThrow(()-> new VendorRequestNotFoundException("There is no vendor request with id:" + id));
    }

    @Override
    public VendorRequestDTO save(VendorRequest vendorRequest) {
        VendorRequest savedVendorRequest = vendorRequestRepository.save(vendorRequest);
        return  VendorRequestDTO.builder()
                .email(savedVendorRequest.getUserDetails().getEmail())
                .createdAt(savedVendorRequest.getCreatedAt())
                .respondedAt(savedVendorRequest.getRespondedAt())
                .vendorRoleStatus(savedVendorRequest.getVendorRoleStatus())
                .id(savedVendorRequest.getId())
                .build();
    }

    @Override
    public VendorRequest findByEmail(String email) {
        return vendorRequestRepository.findVendorRequestByUserDetails_User_Email(email);
    }


    @Override
    public boolean existsByEmail(String email) {
        return vendorRequestRepository.existsVendorRequestByUserDetails_User_Email(email);
    }

    @Override
    public VendorRequest findByEmailForLastRequest(String email) {
        return vendorRequestRepository.findFirstByUserDetails_User_EmailOrderByCreatedAtDesc(email).orElseThrow(()->new VendorRequestNotFoundException("Vendor request not found with this email:" + email));
    }
}

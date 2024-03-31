package com.example.kamil.user.service;

import com.example.kamil.common.response.BaseResponse;
import com.example.kamil.user.model.dto.VendorRequestDTO;
import com.example.kamil.user.model.entity.VendorRequest;

import java.util.List;

public interface VendorRequestService {
    VendorRequestDTO save(VendorRequest vendorRequest);
    List<VendorRequest> findUnreadMessages();
    VendorRequest getById(Long id);
    VendorRequest findByEmail(String email);
    boolean existsByEmail(String email);
    VendorRequest findByEmailForLastRequest(String email);
    List<VendorRequestDTO> getAllVendorRequestsByUserEmail(String email);

    List<VendorRequestDTO> getAllVendorRequests();
}

package com.example.kamil.user.utils.converter;

import com.example.kamil.user.model.dto.VendorRequestDTO;
import com.example.kamil.user.model.entity.VendorRequest;
import jakarta.transaction.Transactional;

public class VendorRequestDTOConverter {

    public static VendorRequestDTO convert(VendorRequest vendorRequest){
        return VendorRequestDTO.builder()
                .id(vendorRequest.getId())
                .vendorRoleStatus(vendorRequest.getVendorRoleStatus())
                .respondedAt(vendorRequest.getRespondedAt())
                .createdAt(vendorRequest.getCreatedAt())
                .email(vendorRequest.getUserDetails().getUser().getEmail())
                .build();
    }
}

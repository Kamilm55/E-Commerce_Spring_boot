package com.example.kamil.user.service;

import com.example.kamil.user.model.entity.VendorRequest;

import java.util.List;

public interface VendorRequestService {
    void insertVendorRequest(VendorRequest vendorRequest);

    List<VendorRequest> findUnreadMessages();
}

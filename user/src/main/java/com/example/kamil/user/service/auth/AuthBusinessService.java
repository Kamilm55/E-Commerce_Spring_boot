package com.example.kamil.user.service.auth;


import com.example.kamil.user.model.dto.LoginResponse;
import com.example.kamil.user.model.payload.LoginPayload;
import com.example.kamil.user.model.payload.RegisterPayload;

public interface AuthBusinessService {
    LoginResponse login (LoginPayload payload);

    void register(RegisterPayload payload);

    void setAuthentication(String email);

}

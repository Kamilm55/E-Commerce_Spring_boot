package com.example.kamil.user.controller;

import com.example.kamil.user.model.dto.LoginResponse;
import com.example.kamil.user.model.payload.LoginPayload;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.service.auth.AuthBusinessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/auth") // access anonymous users (non-authenticated)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthBusinessService authBusinessService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginPayload payload){
        return ResponseEntity.ok(authBusinessService.login(payload));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterPayload payload){
        authBusinessService.register(payload);

        return ResponseEntity.created(URI.create("/v1/users/" + payload.getEmail())).build();
    }

    //////

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
      //TODO:IMPLEMENT this

        return ResponseEntity.ok().build();
    }
}

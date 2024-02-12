package com.example.kamil.user.controller;

import com.example.kamil.user.model.dto.LoginResponse;
import com.example.kamil.user.model.payload.LoginPayload;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.service.auth.AuthBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth") // access anonymous users (non-authenticated)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthBusinessService authBusinessService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginPayload payload){
        return ResponseEntity.ok(authBusinessService.login(payload));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterPayload payload){
        authBusinessService.register(payload);

        return ResponseEntity.ok().build();
    }

    //////

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       //Todo: implement log out

        log.info("{} email log out (TEST)",userDetails.getUsername());

        return ResponseEntity.ok().build();
    }
}

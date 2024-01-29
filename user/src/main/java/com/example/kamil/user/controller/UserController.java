package com.example.kamil.user.controller;

import com.example.kamil.user.entity.User;
import com.example.kamil.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }
}

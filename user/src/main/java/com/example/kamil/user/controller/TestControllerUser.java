package com.example.kamil.user.controller;

import com.example.kamil.user.utils.UserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class TestControllerUser {


    @GetMapping("/user")
    public ResponseEntity<String> testUser(){
        return ResponseEntity.ok("user test works");
    }
    @GetMapping("/testAdmin")
    public ResponseEntity<String> testAdmin(){
        return ResponseEntity.ok("testAdmin test works");
    }

}

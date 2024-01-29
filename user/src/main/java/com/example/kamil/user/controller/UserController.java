package com.example.kamil.user.controller;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.User;
import com.example.kamil.user.payload.CreateUserRequest;
import com.example.kamil.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody CreateUserRequest createUserRequest){
        userService.insertUser(createUserRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    //todo: delete and inactivate and handle error
}

package com.example.kamil.user.controller;

import com.example.kamil.user.dto.UserDTO;
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
    public ResponseEntity<List<UserDTO>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }
    @GetMapping("/getActiveUsers")
    public ResponseEntity<List<UserDTO>> getActiveUsers(){
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@RequestBody CreateUserRequest createUserRequest){
        userService.insertUser(createUserRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{email}/deleteUser")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email ){
        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{email}/deactivateUser")
    public ResponseEntity<Void> deactivateUser(@PathVariable("email") String email ){
        userService.deactivateUser(email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{email}/activateUser")
    public ResponseEntity<Void> activateUser(@PathVariable("email") String email ){
        userService.activateUser(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}/updateUser")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email,@RequestBody CreateUserRequest userRequest ){
        return ResponseEntity.ok(userService.updateUser(email,userRequest));
    }


}

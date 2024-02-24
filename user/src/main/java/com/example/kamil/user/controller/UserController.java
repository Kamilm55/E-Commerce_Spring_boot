package com.example.kamil.user.controller;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid RegisterPayload userRequest){
        return ResponseEntity
                .created(URI.create("/v1/users/" + userRequest.getEmail()))
                .body( userService.insertUser(userRequest));
    }

    @DeleteMapping(path = "/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email ){
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{email}/deactivateUser")
    public ResponseEntity<Void> deactivateUser(@PathVariable("email") String email ){
        userService.deactivateUser(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{email}/activateUser")
    public ResponseEntity<Void> activateUser(@PathVariable("email") String email ){
        userService.activateUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email,
                                              @RequestBody @Valid RegisterPayload userRequest ){
        return ResponseEntity.ok(userService.updateUser(email,userRequest));
    }


}

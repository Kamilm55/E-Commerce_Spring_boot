package com.example.kamil.user.controller;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.payload.CreateUserRequest;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAll(){
        return ResponseEntity.ok(
                userService.getAll()
                .stream().map(user -> UserDTOConverter.convert(user))
                .collect(Collectors.toList())
        );
        //list<User> -> list<UserDTO>
    }
    @GetMapping("/getActiveUsers")
    public ResponseEntity<List<UserDTO>> getActiveUsers(){
        return ResponseEntity.ok(
                userService.getAll()
                        .stream()
                        .filter(user -> user.isActive())
                        .map(user -> UserDTOConverter.convert(user))
                        .collect(Collectors.toList())
        );
        //list<User> -> list<UserDTO>
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

    @DeleteMapping(path = "/deleteUser/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email ){
        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivateUser/{email}")
    public ResponseEntity<Void> deactivateUser(@PathVariable("email") String email ){
        userService.deactivateUser(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/activateUser/{email}")
    public ResponseEntity<Void> activateUser(@PathVariable("email") String email ){
        userService.activateUser(email);
        return ResponseEntity.ok().build();
    }
    //todo:handle error

}

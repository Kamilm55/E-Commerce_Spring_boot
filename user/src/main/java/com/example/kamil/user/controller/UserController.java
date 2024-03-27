package com.example.kamil.user.controller;

import com.example.kamil.common.response.BaseResponse;
import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.service.sse.SseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SseService sseService;

    @GetMapping("/getAllUsers")
    public BaseResponse<List<UserDTO>> getAll(){
        return BaseResponse.success(userService.getAll());
    }
    @GetMapping("/getActiveUsers")
    public BaseResponse<List<UserDTO>> getActiveUsers(){
        return BaseResponse.success(userService.getActiveUsers());
    }

    @GetMapping("/{email}")
    public BaseResponse<UserDTO> getUser(@PathVariable("email") String email){
        return BaseResponse.success(userService.getUserByEmail(email));
    }

    @PostMapping()
    public BaseResponse<UserDTO> addUser(@RequestBody @Valid RegisterPayload userRequest){
        // refactorThis => 201 =>  ResponseEntity
        //                .created(URI.create("/v1/users/" + userRequest.getEmail()))
        //                .body( userService.insertUser(userRequest))
        return BaseResponse.success();
    }

//    @DeleteMapping(path = "/{email}")
//    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email ){
//        userService.deleteUser(email);
//        // TODO: you cannot delete user which is admin
            // Learn: I use soft deletion. use Deactivate method if you want to delete
            //  Consider implementing a soft deletion mechanism. Instead of immediately deleting a user and their associated data, mark the user as inactive or deleted. This way, you retain information about the user for audit purposes and can potentially restore the account if needed
//        return ResponseEntity.noContent().build();
//    }

    @PatchMapping("/{email}/deactivateUser")
    public BaseResponse<Void> deactivateUser(@PathVariable("email") String email ){
        userService.deactivateUser(email);
        //refactorThis:204
        return  BaseResponse.success();
    }

    @PatchMapping("/{email}/activateUser")
    public BaseResponse<Void> activateUser(@PathVariable("email") String email ){
        userService.activateUser(email);
        return  BaseResponse.success();
    }

    @PutMapping("/{email}")
    public BaseResponse<UserDTO> updateUser(@PathVariable("email") String email,
                                              @RequestBody @Valid RegisterPayload userRequest ){
        return  BaseResponse.success(userService.updateUser(email,userRequest));
    }

   @PatchMapping("/{email}/sendRequestForVendorRole")
   public BaseResponse<Void> sendRequestForVendorRole(@PathVariable String email){
        userService.sendRequestForVendorRole(email); // send message (push notifications) to admins

       return  BaseResponse.success();
   }
    @GetMapping("/getRequestForVendorRole")
    public SseEmitter getRequestForVendorRole()  {
        //todo: once get all not read (REQUESTED status) messaged then change status to READ

        return userService.listenVendorRequestEmitter();
    }

//    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribe() {
//        // Add the emitter to a list of subscribers or handle it in another way
//        return new SseEmitter(Long.MAX_VALUE);
//    }
}

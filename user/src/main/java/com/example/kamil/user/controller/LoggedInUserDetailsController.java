package com.example.kamil.user.controller;

import com.example.kamil.common.response.BaseResponse;
import com.example.kamil.user.model.dto.LoggedInUserDetailsDTO;
import com.example.kamil.user.model.payload.UpdateUserDetailsPayload;
import com.example.kamil.user.service.LoggedInUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/userDetails")
@RequiredArgsConstructor
@Slf4j
public class LoggedInUserDetailsController {
    private final LoggedInUserDetailsService loggedInUserDetailsService;

//    @GetMapping("/getAllUserDetails")
//    public ResponseEntity<List<LoggedInUserDetailsDTO>> getAll(){
//        return ResponseEntity.ok(loggedInUserDetailsService.getAll());
//    }

    @GetMapping("/{email}")
    public BaseResponse<LoggedInUserDetailsDTO> getUserDetails(
            @PathVariable("email")
            String email)
    {
        return BaseResponse.success(loggedInUserDetailsService.getUserDetailsByEmail(email));
    }

    @PutMapping("/{email}")
    public BaseResponse<LoggedInUserDetailsDTO> updateUser(
            @PathVariable("email") String email,
            @RequestBody @Valid UpdateUserDetailsPayload updateUserDetailsPayload ){
        return BaseResponse.success(loggedInUserDetailsService.updateUserUserDetails(email,updateUserDetailsPayload));
    }

    @PatchMapping("/{email}/addAdminRole")
    public BaseResponse<Void> addAdminRole(@PathVariable("email") String email){
        loggedInUserDetailsService.addAdminRole(email);
        //refactorThis: status code must be 204 , ResponseEntity.noContent().build()
        return BaseResponse.success();
    }
    @PatchMapping("/{email}/deleteAdminRole")
    public BaseResponse<Void> deleteAdminRole(@PathVariable("email") String email){
        loggedInUserDetailsService.deleteAdminRole(email);
        return BaseResponse.success();
    }

    @PatchMapping("/{email}/addVendorRole")
    public BaseResponse<Void> addVendorRole(@PathVariable("email") String email){
        loggedInUserDetailsService.addVendorRole(email);
        return BaseResponse.success();
    }
    @PatchMapping("/{email}/deleteVendorRole")
    public BaseResponse<Void> deleteVendorRole(@PathVariable("email") String email){
        loggedInUserDetailsService.deleteVendorRole(email);
        return BaseResponse.success();
    }
    //
    @PatchMapping("/{email}/requestForVendorRole")
    public BaseResponse<Void> requestForVendorRole(@PathVariable("email") String email){
        loggedInUserDetailsService.requestForVendorRole(email);
        return BaseResponse.success();
    }

}

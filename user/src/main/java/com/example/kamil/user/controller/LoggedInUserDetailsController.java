package com.example.kamil.user.controller;

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
    public ResponseEntity<LoggedInUserDetailsDTO> getUserDetails(
            @PathVariable("email")
            String email)
    {
        return ResponseEntity.ok(loggedInUserDetailsService.getUserDetailsByEmail(email));
    }

    @PutMapping("/{email}")
    public ResponseEntity<LoggedInUserDetailsDTO> updateUser(
            @PathVariable("email") String email,
            @RequestBody @Valid UpdateUserDetailsPayload updateUserDetailsPayload ){
        return ResponseEntity.ok(loggedInUserDetailsService.updateUserUserDetails(email,updateUserDetailsPayload));
    }

    @PatchMapping("/{email}/addAdminRole")
    public ResponseEntity<Void> addAdminRole(@PathVariable("email") String email){
        loggedInUserDetailsService.addAdminRole(email);
        return ResponseEntity.noContent().build();
    }
//    @PatchMapping("/{email}/deleteAdminRole")
//    public ResponseEntity<Void> deleteAdminRole(@PathVariable("email") String email){
//        loggedInUserDetailsService.deleteAdminRole(email);
//        return ResponseEntity.noContent().build();
//    }

    @PatchMapping("/{email}/addVendorRole")
    public ResponseEntity<Void> addVendorRole(@PathVariable("email") String email){
        loggedInUserDetailsService.addVendorRole(email);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{email}/deleteVendorRole")
    public ResponseEntity<Void> deleteVendorRole(@PathVariable("email") String email){
        loggedInUserDetailsService.deleteVendorRole(email);
        return ResponseEntity.noContent().build();
    }

}

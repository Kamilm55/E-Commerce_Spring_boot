package com.example.kamil.user;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserModel {

    String id;
    String name;
    String email;
    String pincode;
}

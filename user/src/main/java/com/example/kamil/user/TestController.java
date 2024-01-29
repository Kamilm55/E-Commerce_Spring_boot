package com.example.kamil.user;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class TestController {

    @GetMapping()
    public UserModel get()
    {

        UserModel entity = new UserModel();
        entity.setId("1");
        entity.setName("Kamil");
        entity.setEmail("Kamil@geek");
        entity.setPincode("422 009");

        return entity;
    }
}

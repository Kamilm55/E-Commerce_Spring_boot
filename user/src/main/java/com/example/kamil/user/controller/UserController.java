package com.example.kamil.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kamil.user.dto.UserDTO;
import com.example.kamil.user.entity.UserEntity;

@RestController
@RequestMapping(path = "users")
public class UserController {
	
	@PostMapping
	public void add(@RequestBody UserDTO u ) {
		UserEntity entity = new UserEntity();
		entity.setId(null);
		entity.setName(u.getName());
		entity.setSurname(u.getSurname());
		entity.setUsername(u.getUsername());
		entity.setPassword(u.getPassword());
		entity.setEmail(u.getEmail());
		
	}
}

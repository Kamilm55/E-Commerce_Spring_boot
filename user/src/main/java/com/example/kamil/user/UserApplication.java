package com.example.kamil.user;

import com.example.kamil.user.entity.User;
import com.example.kamil.user.payload.CreateUserRequest;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class UserApplication implements CommandLineRunner {

	private final UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
//		CreateUserRequest userRequest = new  CreateUserRequest("username","s","ssd","sad");
//
//		userService.insertUser(userRequest);
//
//		System.out.println(userService.getAll());

//		userService.insertUser(
//				CreateUserRequest.builder()
//						.username("kamil")
//						.email("saddas")
//						.firstName("sad")
//						.lastName("dasdsad")
//						.build()
//		);
//		userService.getAll()
//				.stream()
////				.filter(user -> user.isActive())
//				.forEach(user -> System.out.println(user.isActive()));


	}
}

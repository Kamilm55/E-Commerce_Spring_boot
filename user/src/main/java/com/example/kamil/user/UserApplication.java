package com.example.kamil.user;

import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.utils.PublicPrivateKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		userService.insertUser(
				RegisterPayload.builder()
						.username("kamil")
						.password("pass")
						.email("email1@gmail.com")
						.firstName("sad")
						.lastName("dasdsad")
						.build()
		);

		System.out.println(PublicPrivateKeyUtil.getPublicKey());
		System.out.println();
		System.out.println(PublicPrivateKeyUtil.getPrivateKey());
//		userService.getAll()
//				.stream()
////				.filter(user -> user.isActive())
//				.forEach(user -> System.out.println(user.isActive()));


	}
}

package com.example.kamil.user;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.service.LoggedInUserDetailsService;
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
	private final LoggedInUserDetailsService loggedInUserDetailsService;
	private final UserRepository userRepository;
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

		UserDTO insertedUser = userService.insertUser(
				RegisterPayload.builder()
						.username("kamil")
						.password("pass")
						.email("email1@gmail.com")
						.firstName("sad")
						.lastName("dasdsad")
						.build()
		);
		UserDTO userDTO2 = userService.insertUser(
				RegisterPayload.builder()
						.username("admin")
						.password("pass")
						.email("admin@gmail.com")
						.firstName("admin")
						.lastName("sad")
						.build()
		);

		// ADD ADMIN ROLE
		User user2 = userService.getUserByEmailForUserDetails(userDTO2.getEmail());
		User addAdminRole = userService.addAdminRole(user2);


		//todo: why when i print it works otherwise not?
		// ->
		//   Printing causes throw lazy init exception
		System.out.println(loggedInUserDetailsService.getUserDetails(
				userService.getUserByEmailForUserDetails(insertedUser.getEmail())
		));



//		System.out.println(insertedUser);

	}
}

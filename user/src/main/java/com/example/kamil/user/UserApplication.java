package com.example.kamil.user;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.entity.UserNotification;
import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.repository.LoggedInUserDetailsRepository;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.repository.VendorRequestRepository;
import com.example.kamil.user.service.LoggedInUserDetailsService;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.utils.mapper.UserNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class UserApplication implements CommandLineRunner {

	private final UserService userService;
	private final LoggedInUserDetailsService loggedInUserDetailsService;
	private final LoggedInUserDetailsRepository loggedInUserDetailsRepository;
	private final UserRepository userRepository;
	private final VendorRequestRepository vendorRequestRepository;
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

		// INSERT USERS
		UserDTO insertedUser = userService.insertUser(
				RegisterPayload.builder()
						.username("kamil")
						.password("password")
						.email("email1@gmail.com")
						.firstName("sad")
						.lastName("dasdsad")
						.build()
		);
		UserDTO userDTO2 = userService.insertUser(
				RegisterPayload.builder()
						.username("admin")
						.password("password")
						.email("admin@gmail.com")
						.firstName("admin")
						.lastName("sad")
						.build()
		);

		UserDTO superAdmin = userService.insertUser(
				RegisterPayload.builder()
						.username("superAdmin")
						.password("password")
						.email("superAdmin@gmail.com")
						.firstName("super")
						.lastName("Admin")
						.build()
		);
//		// ADD ADMIN ROLE
//		User user2 = userService.getUserByEmailForUserDetails(userDTO2.getEmail());
		 loggedInUserDetailsService.addAdminRole(userDTO2.getEmail());
		// ADD SUPER ADMIN ROLE
		loggedInUserDetailsService.addSuperAdminRole(superAdmin.getEmail());


		// GET DETAILS OF USERS
		// Learn: We can get lazy loaded field with the help of query
		//  outside of active hibernate session (@transactional)
//		System.out.println(loggedInUserDetailsRepository.findByUserEmail(insertedUser.getEmail()));
//		System.out.println(loggedInUserDetailsRepository.findByUserEmail(user2.getEmail()));


		// PROBLEM SOLVED , BUT IT IS NOT BEST PRACTICE
		// todo: why inside commence method response come with status 200 ?

		// GET USER 1
		//UserDTO user = userService.getUserByEmail(insertedUser.getEmail());

		////////////////////////////
//		userService.sendRequestForVendorRole(userDTO2.getEmail());
//
//		userService.sendRequestForVendorRole(userDTO2.getEmail());
//
//		userService.sendRequestForVendorRole(userDTO2.getEmail());
//		System.out.println(vendorRequestRepository.findFirstByUserDetails_User_EmailOrderByCreatedAtDesc(userDTO2.getEmail()));

		///////////

		//todo: why when i print it works otherwise not?
		// ->
		//   Printing causes throw lazy init exception
		//Learn: we can get lazy init obj only in active hibernate session,in @Transactional annotation,
		// outside of this it throw lazy init exception
//		System.out.println(
//		loggedInUserDetailsService.getUserDetails(
//				userService.getUserByEmailForUserDetails(insertedUser.getEmail())
//		);
//		);

	}
}

package com.example.kamil.user.dto;

import lombok.*;

@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	private String username;
	private String email;

	//private String password;
	private String firstName;
	private String lastName;

}

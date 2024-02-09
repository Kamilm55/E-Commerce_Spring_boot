package com.example.kamil.user.model.dto;

import lombok.*;

@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	private String username;
	private String email;

	private String firstName;
	private String lastName;

}

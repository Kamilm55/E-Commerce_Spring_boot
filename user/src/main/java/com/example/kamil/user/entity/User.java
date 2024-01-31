package com.example.kamil.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Data
public class User  {
	@Id
	@GeneratedValue()
	private Long id;

	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String email;

	//private String password;
	private String firstName;
	private String lastName;
	private Boolean isActive;


}


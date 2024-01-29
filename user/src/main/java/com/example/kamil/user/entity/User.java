package com.example.kamil.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
public class User  {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private Long id;

	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String email;

	//private String password;
	private String firstName;
	private String lastName;


	
	

}

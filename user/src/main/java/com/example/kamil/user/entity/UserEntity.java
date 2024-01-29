package com.example.kamil.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class UserEntity  {


	
	@Id
	private String username;
	
	private String password;
	
	private String email;
	
	

}

package com.example.kamil.user.model.entity;

import com.example.kamil.user.model.security.LoggedInUserDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@EqualsAndHashCode
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
	private String password;

	private String firstName;
	private String lastName;
	private Boolean isActive;

	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private Set<LoggedInUserDetails> userDetails;

}


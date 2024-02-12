package com.example.kamil.user.model.entity;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "userDetails") // this is important for operations in db
@ToString(exclude = "userDetails")
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

	@OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	@JoinColumn(name = "userDetails_id" , referencedColumnName = "id",nullable = false)
	@JsonIgnore // Ignore during serialization to break the loop
	private LoggedInUserDetails userDetails;

}


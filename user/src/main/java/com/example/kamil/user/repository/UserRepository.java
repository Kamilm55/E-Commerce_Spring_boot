package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kamil.user.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

   // boolean existsDistinctByEmail(String email);
}

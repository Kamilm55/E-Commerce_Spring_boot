package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedInUserDetailsRepository extends JpaRepository<LoggedInUserDetails,Long> {
    @Query("SELECT lud FROM LoggedInUserDetails lud JOIN lud.user u WHERE u.email = :email")
    LoggedInUserDetails findByUserEmail(String email);
}

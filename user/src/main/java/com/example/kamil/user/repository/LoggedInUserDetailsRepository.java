package com.example.kamil.user.repository;

import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedInUserDetailsRepository extends JpaRepository<LoggedInUserDetails,Long> {
}

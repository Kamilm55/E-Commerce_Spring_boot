package com.example.kamil.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kamil.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{

}

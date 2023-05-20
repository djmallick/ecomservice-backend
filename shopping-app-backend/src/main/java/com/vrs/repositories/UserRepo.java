package com.vrs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}

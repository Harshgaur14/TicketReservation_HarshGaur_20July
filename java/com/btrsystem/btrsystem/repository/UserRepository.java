package com.btrsystem.btrsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btrsystem.btrsystem.models.User;

import java.util.Optional;


public interface UserRepository  extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);


	User findByEmail(String email);
	
//	Optional<User> findByEmail(String email);
	
}

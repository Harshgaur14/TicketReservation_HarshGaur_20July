package com.pmu.pmu.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmu.pmu.models.User;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);

	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);


	User findByEmail(String email);
	
//	Optional<User> findByEmail(String email);
	
}

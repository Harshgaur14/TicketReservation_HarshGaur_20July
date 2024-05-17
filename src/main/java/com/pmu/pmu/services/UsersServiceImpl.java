package com.pmu.pmu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.pmu.pmu.models.User;
import com.pmu.pmu.repository.UserRepository;

@Service
public class UsersServiceImpl implements UsersService{

	@Autowired
	private UserRepository usersRepository;

	
	@Override
	public User getUserByUsername(String username) {
		Optional<User> opt = usersRepository.findByUsername(username);
		return opt.get();
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User saveUser(User user) {
		
		return usersRepository.save(user);
	}

	@Override
	public List<User> getUsersWithRoles(String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameExists(String username) {
		return usersRepository.existsByUsername(username);
	}

	@Override
	public boolean isEmailExists(String email) {
		return usersRepository.existsByEmail(email);
	}
	
	

}

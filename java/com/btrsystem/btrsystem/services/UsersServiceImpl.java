package com.btrsystem.btrsystem.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.btrsystem.btrsystem.models.User;
import com.btrsystem.btrsystem.repository.UserRepository;


@Service
public class UsersServiceImpl implements UsersService{

	@Autowired
	private UserRepository usersRepository;
	

	
	@Override
	public User getUserByUsername(String username) {
		Optional<User> opt = usersRepository.findByUsername(username);
		return opt.get();
	}

//	@Override
//	public User getUserByEmail(String email) {
//		// TODO Auto-generated method stub
//		Optional<User> opt = usersRepository.findByEmail(email);
//		return opt.get();
//	}

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

	@Override
    public User getUserById(int userId) {
        return usersRepository.findById(userId).orElse(null);
    }

	
	

}

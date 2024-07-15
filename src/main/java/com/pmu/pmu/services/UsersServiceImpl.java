package com.pmu.pmu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.pmu.pmu.models.User;
import com.pmu.pmu.repository.UserRepository;

@Service
public class UsersServiceImpl implements UsersService{

	@Autowired
	private UserRepository usersRepository;
	@Autowired
	private PasswordEncoder encoder;

	
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
		public void updatePassword(String userId, String newPassword) {
        // Find the user by ID
        User user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        	System.out.println(newPassword+"-----"+user.getEmail());
        // Encrypt the new password
        String encodedPassword = encoder.encode(newPassword);

        // Update the user's password
        user.setPassword(encodedPassword);

        // Save the updated user
        usersRepository.save(user);
    }

	public void resetPassword(String email, String newPassword) {
        User user = usersRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            usersRepository.save(user);
            System.out.println("Password updated successfully for user: " + email);
        } else {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
    }
	
	

}

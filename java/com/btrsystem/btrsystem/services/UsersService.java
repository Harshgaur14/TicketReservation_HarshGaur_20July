package com.btrsystem.btrsystem.services;

import java.util.List;

import com.btrsystem.btrsystem.models.User;



public interface UsersService {

	
public User getUserByUsername(String username);
	
	//public User getUserByEmail(String email);
	
	public User saveUser(User user);
	
	public List<User> getUsersWithRoles(String role);
	
	public boolean isUsernameExists(String username);
	
	public boolean isEmailExists(String email);

	public User getUserById(int userId);
	
//	public void updatePassword(String userId,String newPassword);
//
//	public void resetPassword(String email, String newPassword);
//	
	
}

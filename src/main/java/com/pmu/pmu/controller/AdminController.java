package com.pmu.pmu.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pmu.pmu.models.User;
import com.pmu.pmu.services.UsersService;

@RestController
public class AdminController {

	@Autowired
	private UsersService usersService;
	@GetMapping("/profile")
	public ResponseEntity<?> getUserInfo(Principal user){
		User userObj=(User)usersService.getUserByUsername(user.getName());
		User userInfo=new User();
		userInfo.setEmail(userObj.getEmail());

		userInfo.setUsername(userObj.getUsername());

		userInfo.setPassword(userObj.getPassword());
		userInfo.setRole(userObj.getRole());
		System.out.println("profile mapping");
	return ResponseEntity.ok(userInfo);
	}
	
	
}

package com.btrsystem.btrsystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btrsystem.btrsystem.models.User;
import com.btrsystem.btrsystem.payload.request.SignupRequest;
import com.btrsystem.btrsystem.payload.response.MessageResponse;
import com.btrsystem.btrsystem.payload.token.TokenBlacklist;
import com.btrsystem.btrsystem.security.jwt.JwtUtils;
import com.btrsystem.btrsystem.services.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UsersService usersService;
	
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	@Autowired
	JwtUtils jwtUtils;
	
	
	
	@Autowired
	static
	TokenBlacklist tokenBlacklist;
	
	

	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,BindingResult bindingResult) {
	    
		  if (bindingResult.hasErrors()) {
		        Map<String, String> errors = new HashMap<>();
		        bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		        System.out.println(ResponseEntity.badRequest().body(errors));
		        return ResponseEntity.badRequest().body(errors);
		    }
		if (usersService.isUsernameExists(signUpRequest.getUsername())) {
			return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (usersService.isEmailExists(signUpRequest.getEmail())) {
	    	return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    User user = new User();
	    user.setEmail(signUpRequest.getEmail());
	    user.setUsername(signUpRequest.getUsername());
	    user.setPassword(encoder.encode(signUpRequest.getPassword()));
	    user.setRole("ROLE_USER");
	    user.setPhoneNo(signUpRequest.getPhoneNo());
	    
	    usersService.saveUser(user);
	   
	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
	
		//logout 
	@PreAuthorize("hasRole('ROLE_USER')")
	 @GetMapping("/logout")
	    public static void logout(HttpServletRequest request, HttpServletResponse response) {
	        // Extract JWT token from the Authorization header
	        String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            // Remove "Bearer " prefix
	            String jwtToken = authorizationHeader.substring(7);
	            
	            // Now you have the JWT token, you can perform further actions like invalidation
	            TokenBlacklist.invalidateToken(jwtToken);
	            
	        }
	    }
	
	

	

}

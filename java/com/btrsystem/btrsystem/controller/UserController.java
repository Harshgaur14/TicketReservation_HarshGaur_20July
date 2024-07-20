package com.btrsystem.btrsystem.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btrsystem.btrsystem.dto.ReservationDTO;
import com.btrsystem.btrsystem.models.Reservation;
import com.btrsystem.btrsystem.models.Trip;
import com.btrsystem.btrsystem.models.User;
import com.btrsystem.btrsystem.payload.request.SignupRequest;
import com.btrsystem.btrsystem.payload.response.MessageResponse;
import com.btrsystem.btrsystem.payload.token.TokenBlacklist;
import com.btrsystem.btrsystem.security.jwt.JwtUtils;
import com.btrsystem.btrsystem.services.ReservationService;
import com.btrsystem.btrsystem.services.TripService;
import com.btrsystem.btrsystem.services.UsersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	

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
	
	

    @Autowired
    private TripService tripService;

    @Autowired
    private ReservationService reservationService;

    
    @PostMapping("/reserve")
    public Reservation createReservation(ReservationDTO reservationDTO) {
        // Find the Trip and User entities using the respective services
        Trip trip = tripService.findTripsByBusId(reservationDTO.getTripId());
        User user = usersService.getUserById(reservationDTO.getUserId());

        if (trip != null && user != null) {
            // Create a new Reservation entity
            Reservation reservation = new Reservation();
            reservation.setTrip(trip);
            reservation.setUser(user);
            reservation.setNumberOfSeats(reservationDTO.getNumberOfSeats());
            reservation.setTotalAmount(reservationDTO.getTotalAmount());

            // Save and return the Reservation
            return reservationService.save(reservation);
        } else {
            // Handle the case where Trip or User is not found
            throw new RuntimeException("Trip or User not found");
        }
    }
	
	

	

}

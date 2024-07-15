package com.pmu.pmu.controller;

import org.springframework.web.bind.annotation.*;

import com.pmu.pmu.emailservice.EmailSenderService;
import com.pmu.pmu.models.PasswordResetToken;
import com.pmu.pmu.payload.request.PasswordChange;
import com.pmu.pmu.payload.request.ResetPassword;
import com.pmu.pmu.services.PasswordResetTokenService;
import com.pmu.pmu.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@CrossOrigin(
		origins = { "http://10.226.49.255:3000","http://localhost:3001","http://localhost:3000","http://10.226.39.57:3000","http://10.226.48.52:3000","http://10.226.39.57:3001" }, 
		maxAge = 3600, 
		allowCredentials = "true", 
		allowedHeaders = "*")
@RestController
@RequestMapping("/password")
public class PasswordResetController {

	@Autowired
	private PasswordEncoder encoder;
	
    @Autowired
    private PasswordResetTokenService tokenService;

  @Autowired
	private EmailSenderService emailsenderService;

	@Autowired
	private UsersService usersService;
    
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        // Generate token and set expiry time
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(10);
        	System.out.println(email);
        // Save token to database
        tokenService.createPasswordResetToken(email, token, expiryDateTime);
        String resetLink = "http://10.226.48.52:3000/password/reset?token=" + token;
        // Send email with password reset link containing token
        // Example: constructEmail(token, email);
        emailsenderService.sendEmail(email,"Password Reset Request",resetLink);
        

        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword) {
        String token=resetPassword.getTokenurl();
        String newPassword=resetPassword.getPassword();
    	PasswordResetToken resetToken = tokenService.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
        	System.out.println("expired");
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
        String email=tokenService.findByToken(token).getEmail();
        System.out.println("User email is "+token+"----"+newPassword);
        
        // Perform password reset for the user with resetToken.getEmail() and newPassword
        // Example: userService.resetPassword(resetToken.getEmail(), newPassword);
        usersService.resetPassword(email,encoder.encode(newPassword));
        // Delete the token after successful password reset
        tokenService.deleteToken(token);

        return ResponseEntity.ok("Password reset successfully.");
    }

    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	 @PostMapping("/change")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChange passwordChange) {
			// Perform password reset for the user with resetToken.getEmail() and newPassword
			// Example: userService.resetPassword(resetToken.getEmail(), newPassword);
    	
    	
    	System.out.println("hwy");
    	
			usersService.resetPassword(passwordChange.getEmail(),encoder.encode(passwordChange.getPassword()));
			// Delete the token after successful password reset
			
			return ResponseEntity.ok("Password change successfully.");
			}

}







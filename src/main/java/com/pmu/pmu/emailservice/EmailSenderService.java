package com.pmu.pmu.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String toEmail,
						  String subject,
						  String body)
	{
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("harshgaur@cdac.in"); //sender email
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
		
		System.out.println("mail sent successfully...");
	}
	 public void sendResetLink(String to, String token) {
	        String resetUrl = "http://10.226.39.57/reset-password?token=" + token;
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("Password Reset Request");
	        message.setText("Click the following link to reset your password: " + resetUrl);
	        mailSender.send(message);
	    }

	
}

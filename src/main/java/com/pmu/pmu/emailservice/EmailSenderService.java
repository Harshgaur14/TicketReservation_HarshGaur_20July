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
		message.setFrom("cybergyan-noida@cdac.in"); //sender email
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
		
		System.out.println("mail sent successfully...");
	}
	

//	String rejectionEmail = "Dear Participant,\n\n" +
//	        "Thank you for showing your interest in the course \"Ethical Hacking and Penetration Testing\".\n\n" +
//	        "We are sorry to inform you that your registration request for the course has been rejected due to any of the following reasons:\n\n" +
//	        "1. You are not eligible for the course.\n" +
//	        "2. The documents submitted by you are incorrect.\n\n" +
//	        "For any query, please send an email to at cybergyan-noida@cdac.in.\n\n" +
//	        "Thanks & Regards,\n" +
//	        "CyberGyan Team";
//
//	senderService.sendEmail(rejectedStudentDtls.getEmailAddress(), "Cyber Gyan", rejectionEmail);
	
}

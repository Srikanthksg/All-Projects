package com.sri.ms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	
	public void sendWelcomeEmail(String toEmail, String name) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Welcome to our Platform");
		message.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\nAuthify Team");
		mailSender.send(message);
	}
	
	public void sendResetOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Password reset OTP");
		message.setText("Your otp for resetting your password is "+otp+". Use this otp to proceed with resetting your password");
		mailSender.send(message);
	}
	
	public void sendOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Account verification OTP");
		message.setText("Your otp is "+otp+". Verify your account");
		mailSender.send(message);
	}
	
	
}

package com.sri.ms.service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sri.ms.entity.UserEntity;
import com.sri.ms.io.ProfileRequest;
import com.sri.ms.io.ProfileResponse;
import com.sri.ms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;

	@Override
	public ProfileResponse createProfile(ProfileRequest request) {
		UserEntity newProfile = convertToUserEntity(request);
		if(! userRepository.existsByEmail(request.getEmail())) {
			newProfile = userRepository.save(newProfile);
			return convertToProfileResponse(newProfile);
		}
		throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist");
		
	}

	private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
		
		return ProfileResponse.builder()
				.name(newProfile.getName())
				.email(newProfile.getEmail())
				.userId(newProfile.getUserId())
				.isAccountVerified(newProfile.getIsAccountVerified())
				.build();
	}

	private UserEntity convertToUserEntity(ProfileRequest request) {
		return UserEntity.builder()
		          .email(request.getEmail())
		          .userId(UUID.randomUUID().toString())
		          .name(request.getName())
		          .password(passwordEncoder.encode(request.getPassword()))
		          .isAccountVerified(false)
		          .resetOtpExpireAt(0L)
		          .verifyOtp(null)
		          .verifyOtpExpireAt(0L)
		          .resetOtp(null)
		          .build();
	}

	
	
	
	@Override
	public ProfileResponse getProfile(String email) {
		
		UserEntity existingUser = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
		return convertToProfileResponse(existingUser);
	}
	

	@Override
	public void sendResetOtp(String email) {
		UserEntity existingEntity = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
		
		//Generate 6 digit otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		
		//calculate expiry time(current time +15 min in milliseconds)
		long expiryTime = System.currentTimeMillis()+(15*60*1000);
		
		//update the profileEntity
		existingEntity.setResetOtp(otp);
		existingEntity.setResetOtpExpireAt(expiryTime);
		
		// save into the database
		userRepository.save(existingEntity);
		
		try {
			// TODO: send the reset otp email
			emailService.sendResetOtpEmail(existingEntity.getEmail(), otp);
			
		}catch(Exception ex) {
			throw new RuntimeException("Unable to send email");
		}
		
		
		
	}

	@Override
	public void resetPassword(String email, String otp, String newPassword) {

		UserEntity existingUser = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
		
		if(existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)) {
			throw new RuntimeException("Invalid Otp");
		}
		if(existingUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException("OTP Expired");
		}
		existingUser.setPassword(passwordEncoder.encode(newPassword));
		existingUser.setResetOtp(null);
		existingUser.setResetOtpExpireAt(0L);
		
		userRepository.save(existingUser);
		
		
	}

	@Override
	public void sendOtp(String email) {
		UserEntity existingUser = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
		if(existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()) {
			return;
		}
		//Generate 6 digit otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

		//calculate expiry time(current time +24 hrs in milliseconds)
		long expiryTime = System.currentTimeMillis() + (24*60*60*1000);
		
		//Update the user entity
		existingUser.setVerifyOtp(otp);
		existingUser.setVerifyOtpExpireAt(expiryTime);
		
		userRepository.save(existingUser);
		
		try {
			emailService.sendOtpEmail(existingUser.getEmail(), otp);
		}catch(Exception e) {
			throw new RuntimeException("Unable to send email");
		}
		
	}

	@Override
	public void verifyOtp(String email, String otp) {
		UserEntity existingUser = userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
		
		if(existingUser.getVerifyOtp() == null || ! existingUser.getVerifyOtp().equals(otp)) {
			throw new RuntimeException("Invalid OTP");
		}
		
		if(existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException("OTP Expired");
		}
		
		existingUser.setIsAccountVerified(true);
		existingUser.setVerifyOtp(null);
		existingUser.setVerifyOtpExpireAt(0L);
		
		userRepository.save(existingUser);
		
	}

	

}

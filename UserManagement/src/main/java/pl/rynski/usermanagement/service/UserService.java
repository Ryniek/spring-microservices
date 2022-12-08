package pl.rynski.usermanagement.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.rynski.usermanagement.dto.UserDto;
import pl.rynski.usermanagement.model.User;
import pl.rynski.usermanagement.repository.UserRepository;

@Service
public record UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

	public UserDto createUser(UserDto userDto) {
		
		userDto.setUserId(UUID.randomUUID().toString());
		User user = User.builder()
				.email(userDto.getEmail())
				.userId(userDto.getUserId())
				.encryptedPassword(passwordEncoder.encode(userDto.getPassword()))
				.build();
		
		User savedUser = userRepository.save(user);
		return UserDto.builder()
				.email(savedUser.getEmail())
				.userId(savedUser.getUserId())
				.build();
	}
	
	public UserDto getUserDetailsByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		return UserDto.builder()
				.email(user.getEmail())
				.userId(user.getUserId())
				.build();
	}
}

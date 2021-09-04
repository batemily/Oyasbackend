package com.example.security.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.config.Message;
import com.example.security.dto.LoginRequest;
import com.example.security.dto.LoginResponse;
import com.example.security.dto.RegisterRequest;
import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserDetailsImpl;
import com.example.security.util.JwtUtil;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {	
	
	@Autowired
	private UserRepository userRepository;
	


	@PreAuthorize("isAuthenticated()")
    @GetMapping()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	@PreAuthorize("isAuthenticated()")
    @GetMapping("/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email);
    }


	@PreAuthorize("isAuthenticated()")
	@PostMapping()
	public User createUser(@Validated @RequestBody User user) {
		return userRepository.save(user);
	}




	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id,
			@Validated @RequestBody User userDetails) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
		user.setEmail(userDetails.getEmail());
		user.setLastName(userDetails.getLastName());
		user.setFirstName(userDetails.getFirstName());
		user.setTel(userDetails.getTel());
		user.setRoles(userDetails.getRoles());
		final User updateUser = userRepository.save(user);
		return ResponseEntity.ok(updateUser);
	}


	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

		userRepository.delete(user);
		return true;
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/blockUser")
	public void blockUser(@RequestBody User user) throws ResourceNotFoundException {
		User u = userRepository.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getId()));
		u.setActive(true);
		userRepository.saveAndFlush(u);
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/unlockUser")
	public void unlockUser(@RequestBody User user) throws ResourceNotFoundException {
		User u = userRepository.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getId()));
		u.setActive(false);
		userRepository.saveAndFlush(u);
	}
	

}

package com.example.security.controller;

import java.util.*;

import com.example.security.dto.ChangePasswordRequest;
import com.example.security.service.IUserService;
import exception.EmailException;
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
import org.springframework.web.bind.annotation.*;

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

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {	
	
	@Autowired
	private IUserService userService;


	@Autowired
	private UserRepository userRepository;


	@PreAuthorize("isAuthenticated()")
	@GetMapping("/by-id/{id}")
	public User getUserById(@PathVariable Long id) throws ResourceNotFoundException {
		return  userService.getUserById(id);
	}


	@PreAuthorize("isAuthenticated()")
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

	@PreAuthorize("isAuthenticated()")
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) throws ResourceNotFoundException {
        return userService.getUserByEmail(email);
    }


	@PreAuthorize("isAuthenticated()")
	@PostMapping()
	public User createUser(@Validated @RequestBody User user) {
		return userService.createUser(user);
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id,
			@Validated @RequestBody User userDetails) throws ResourceNotFoundException {
		final User updateUser = userService.updateUser(userDetails);
		return ResponseEntity.ok(updateUser);
	}


	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") Long id){
		userService.deleteUser(id);
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/blockUser")
	public void blockUser(@RequestBody User user) throws ResourceNotFoundException {
		userService.blockUser(user);
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/unlockUser")
	public void unlockUser(@RequestBody User user) throws ResourceNotFoundException {
		userService.unlockUser(user);
	}



	@GetMapping("/resetPassword")
	public void resetPassword(@RequestParam("email") String email) throws ResourceNotFoundException, MessagingException {
		userService.resetPassword(email);
	}


	@PreAuthorize("isAuthenticated()")
	@PutMapping("/changePassword")
	public void changePassword(@RequestBody ChangePasswordRequest passwordRequest) throws ResourceNotFoundException {
		userService.changePassword(passwordRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/changeUserInfo")
	public User changeUserInfo(@RequestBody User userRequest , @RequestParam("connectedUserMail") String connectedUserMail) throws ResourceNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
		if(optionalUser.isPresent() && !connectedUserMail.equals(userRequest.getEmail())){
			throw new EmailException("Cet email existe deja");
		}
		return userService.updateUser(userRequest);
	}



	

}

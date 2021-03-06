package com.example.security.controller;

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
import exception.UserNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class LoginController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private UserDetailsImpl userDetailsImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        User user = User.builder().build();
        if(optionalUser.isPresent()){
             user = optionalUser.get();
            if(!user.isActive()){
                throw new UserNotActiveException("Ce utilisateur n'est pas actif");
            }
        }
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        final UserDetails userDetails = userDetailsImpl.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return  LoginResponse.builder()
			        		.jwt(jwt)
			        		.nom(user.getFirstName())
			        		.prenom(user.getLastName())
			        		.email(user.getEmail())
			        		.build();

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest data) {
        if(userRepository.existsByEmail(data.getEmail())) {
            return ResponseEntity.badRequest().body(new Message("Error: This Email is already in use!"));
        }
        Role role = roleRepository.findRoleByName("user");
        User user = new User(data.getEmail(), data.getFirstName(), data.getLastName(),  encoder.encode(data.getPassword()), data.getTel(), Collections.singletonList(role),true);
        userRepository.save(user);
        return ResponseEntity.ok(new Message ("User registered successfully!"));
    }

}

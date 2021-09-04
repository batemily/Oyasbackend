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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


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

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        final UserDetails userDetails = userDetailsImpl.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new LoginResponse(jwt);

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest data) {
        if(userRepository.existsByEmail(data.getEmail())) {
            return ResponseEntity.badRequest().body(new Message("Error: This Email is already in use!"));
        }
        Role role = roleRepository.findRoleByName("user");
        User user = new User(data.getEmail(), data.getFirstname(), data.getLastname(),  encoder.encode(data.getPassword()), data.getTel(), Collections.singletonList(role));
        userRepository.save(user);
        return ResponseEntity.ok(new Message ("User registered successfully!"));
    }

}

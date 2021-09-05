package com.example.security.service.impl;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import com.example.security.service.IUserService;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;



    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) throws ResourceNotFoundException {
       return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " +email));
    }

    @Override
    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id)  {
        userRepository.deleteById(id);
    }

    @Override
    public void blockUser(User user) throws ResourceNotFoundException {
        User u = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getId()));
        u.setActive(true);
        userRepository.saveAndFlush(u);
    }

    @Override
    public void unlockUser(User user) throws ResourceNotFoundException {
        User u = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getId()));
        u.setActive(false);
        userRepository.saveAndFlush(u);
    }

    @Override
    public void resetPassword(String email) throws ResourceNotFoundException {
        User user = getUserByEmail(email);

    }
}

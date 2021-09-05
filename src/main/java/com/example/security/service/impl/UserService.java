package com.example.security.service.impl;

import com.example.security.dto.ChangePasswordRequest;
import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import com.example.security.service.IMailService;
import com.example.security.service.IUserService;
import com.example.security.util.RandomPasswordGenerator;
import exception.PasswordException;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IMailService mailService;



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
        if(user.getId()==null){
            user.setPassword(encoder.encode(user.getPassword()));
            Role role = roleRepository.findRoleByName("user");
            user.setRoles(Collections.singletonList(role));
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(User user) {
        return createUser(user);
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

    @Transactional
    @Override
    public void resetPassword(String email) throws ResourceNotFoundException, MessagingException {
        User user = getUserByEmail(email);
        String newPassword = RandomPasswordGenerator.generateRandomPassword(9);
        String encodeNewPassword = encoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        mailService.sendUserNewPassword(email , newPassword);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest passwordRequest) throws ResourceNotFoundException {
        User user = getUserByEmail(passwordRequest.getEmail());
        if(encoder.matches(passwordRequest.getOldPassword(),user.getPassword())){
            String encodedPassword = encoder.encode(passwordRequest.getNewPassword());
            user.setPassword(encodedPassword);
            userRepository.saveAndFlush(user);
        }else{
            throw new PasswordException("Veuillez saisir le bon mot de passe");
        }
    }
}

package com.example.security.service;

import com.example.security.dto.ChangePasswordRequest;
import com.example.security.model.User;
import exception.ResourceNotFoundException;

import javax.mail.MessagingException;
import java.util.List;

public interface IUserService {

    List<User> getAllUsers();

    User getUserByEmail(String email) throws ResourceNotFoundException;

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id) ;

    void blockUser(User user) throws ResourceNotFoundException;

    void unlockUser(User user) throws ResourceNotFoundException;

    void resetPassword(String email) throws ResourceNotFoundException, MessagingException;

    void changePassword(ChangePasswordRequest passwordRequest) throws ResourceNotFoundException;

    User getUserById(Long id) throws ResourceNotFoundException;
}

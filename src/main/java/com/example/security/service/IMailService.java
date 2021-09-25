package com.example.security.service;

import javax.mail.MessagingException;

public interface IMailService {
    void sendUserNewPassword(String recipientMail , String newPassword) throws MessagingException;

	void sendAlertMessage(String recipientMail, String message) throws MessagingException;
}

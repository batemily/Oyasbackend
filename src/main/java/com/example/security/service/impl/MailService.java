package com.example.security.service.impl;


import com.example.security.constants.MailConstant;
import com.example.security.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService implements IMailService {

    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderMail;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendUserNewPassword(String recipientMail , String newPassword) throws MessagingException {
        MimeMessage htmlMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(htmlMailMessage, true);
        helper.setTo(recipientMail);
        helper.setSubject(MailConstant.SUBJECT);
        helper.setFrom(senderMail);
        helper.setText(String.format(MailConstant.CONTENT, newPassword, "<a href =\"http://localhost:4200/login\" target=\"_blank\" data-saferedirecturl=\"https://localhost:4200/login\">S'identifier</a>"), true);
        try {
            javaMailSender.send(htmlMailMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

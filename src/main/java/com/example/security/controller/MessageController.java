package com.example.security.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.repository.RoleRepository;
import com.example.security.service.impl.MailService;

@RestController
@RequestMapping("/api/sendMail")
@CrossOrigin("*")
public class MessageController {

	
	@Autowired
	private MailService mailService;
	

    @GetMapping()
    public String sendMail(@RequestParam String message, @RequestParam String recipientMail) throws MessagingException {
    	if ("rain".equals(message)) {
			mailService.sendAlertMessage(recipientMail, "Le niveau d'eau est inférieur à 20%% ans la jarre .");
		} 
    	if ("batt".equals(message)) {
			mailService.sendAlertMessage(recipientMail, "Le niveau de battérie du capteur est inférieur à 20 %% .");
		}
		return message;
    }
    
}
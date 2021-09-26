package com.example.security.controller;

import static com.example.security.constants.Constant.NIVEAU_BATT;
import static com.example.security.constants.Constant.NIVEAU_EAU;

import java.util.Queue;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.service.impl.MailService;
import com.example.security.util.OyasData;

@RestController
@RequestMapping("/api/sendMail")
@CrossOrigin("*")
public class MessageController {

	
	@Autowired
	private MailService mailService;
	
	@Autowired
	Queue<OyasData> queue;

    @GetMapping()
    public String sendMail(@RequestParam String message, @RequestParam String recipientMail) throws MessagingException {
    	if (NIVEAU_EAU.equals(message)) {
			mailService.sendAlertMessage(recipientMail, "Le niveau d'eau est inférieur à 20%% ans la jarre.");
		} 
    	if (NIVEAU_BATT.equals(message)) {
			mailService.sendAlertMessage(recipientMail, "Le niveau de battérie du capteur est inférieur à 20 %% .");
		}
		return message;
    }
    
    
    
    @GetMapping("/data")
    public OyasData getData() {
    	OyasData oyasData;
    	if (queue.size()<2) {
    		oyasData = formatData(queue.peek());
			
		} else {
			oyasData = queue.poll();
		}
		return oyasData;
    }
    
    private OyasData formatData(OyasData oyasData) {
    	Integer a = (int) (oyasData.getRainData()*3.333333333);
		return OyasData.builder()
				
				.rainData(a)
				.battData(oyasData.getBattData())
				.build();
	}



	private void alertMessage(Integer value) {
    	
    }
}
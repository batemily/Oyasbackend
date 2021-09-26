package com.example.security;


import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thethingsnetwork.java.app.lib.Client;
import org.thethingsnetwork.java.app.lib.Message;

import com.example.security.repository.CapteurRepository;
import com.example.security.repository.DonneCapteurRepository;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.RxCapteurRepository;
//import com.example.security.repository.RxCapteurRepository;
import com.example.security.repository.UserRepository;
import com.example.security.util.OyasData;

//import io.jsonwebtoken.lang.Arrays;
//import io.jsonwebtoken.lang.Collections;

@SpringBootApplication
public class SecurityApplication implements CommandLineRunner{

	static String region = "asia-se";
    static String appId = "sensorultra";
    static String accessKey = "ttn-account-v2.g7M2c9s1lvj-ySg3cAc-CmAJw0thqRSFxrjFd3k7AVY";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DonneCapteurRepository donneeCapteurRepository;
	
	@Autowired
	private CapteurRepository capteurRepository;
	
	@Autowired
	private RxCapteurRepository rxCapteurRepository;
	
	@Autowired
	private RoleRepository roleRepository;
		
	@Autowired
	private PasswordEncoder encoder;
	
    private static Logger LOG = LoggerFactory.getLogger(SecurityApplication.class);
	//@Autowired
	//Queue<OyasData> queue;
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		startRetreaveData();
		getQueue().add(OyasData.builder()
				.battData(10)
				.rainData(0)
				.build());
		//	capteurRepository.save(new Capteur(123547L, "Capteur RGX-17"));
		
//		Role role = roleRepository.save(new Role("admin"));
//		//RxCapteur rxCapteur= rxCapteurRepository.save(new RxCapteur(null, "rx1"));
//		User user = new User();
//		user.setEmail("user@gmail.fr");
//		user.setPassword(encoder.encode("password"));
//		user.setRoles(Arrays.asList(role));
//		user.setTel("+216 54 175 896");
//		//user.setRxCapteur(java.util.Collections.singletonList(rxCapteur));
//		userRepository.save(user);
//
//
//		Role r =roleRepository.save(new Role("user"));
//		User u = new User();
//		u.setEmail("ali@gmail.com");
//		u.setPassword(encoder.encode("motdepasse"));
//		u.setRoles(Arrays.asList(r));
//		u.setTel("+216 54 175 457");
//		userRepository.save(u);
//
//		Capteur capteur= new Capteur();
//		capteur.setName("capteur1");
//		capteurRepository.save(capteur);
//
//		RxCapteur rxcapteur= new RxCapteur();
//		rxcapteur.setName("rxcapteur1");
//		rxCapteurRepository.save(rxcapteur);
//
//
//		DonneCapteur donnee= new DonneCapteur();
//		donnee.setNivBat((long) 20);
//		donnee.setNivEau((long)30);
//		donnee.setDateEnvoi(null);
//		donnee.setDateDernRecep(null);
//		donneeCapteurRepository.save(donnee);

		
	}
	private void startRetreaveData() throws URISyntaxException, MqttException {
		Client client = new Client(region, appId, accessKey);
        client.onError((Throwable _error) -> System.err.println("error: " + _error.getMessage()));

        client.onMessage((String devId, Object  data) -> {
        	//System.out.println("Message: " + devId + " " + data);
        	byte[] payload = ((Message) data).getBinary("payload_raw");
        	LOG.info("Message: " + devId + " " + payload[0] + " " + payload[1]);
            getQueue().add(OyasData.builder()
    				.battData(Math.abs(Integer.valueOf(payload[1])))
    				.rainData(Integer.valueOf(payload[0]))
    				.build());
        });
        client.onConnected((MqttClient _client) -> System.out.println("connected !"));
        client.start();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public Queue<OyasData> getQueue() {
		return new LinkedList<OyasData>();
	}
	

}

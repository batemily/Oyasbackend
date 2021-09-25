package com.example.security.service;

import java.util.List;

import javax.mail.MessagingException;

import com.example.security.model.Capteur;

public interface ICapteurService {
	List<Capteur> getAll();

	Capteur getById(Long id);

	Capteur update(Capteur capteur);

	Capteur add(Capteur capteur);

	Capteur getByName(String name);

}

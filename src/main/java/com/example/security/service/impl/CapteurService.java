package com.example.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.security.model.Capteur;
import com.example.security.repository.CapteurRepository;
import com.example.security.service.ICapteurService;

@Service
public class CapteurService implements ICapteurService {

	@Autowired
	private CapteurRepository capteurRepository;
	@Override
	public List<Capteur> getAll() {
		return capteurRepository.findAll();
	}

	@Override
	public Capteur getById(Long id) {
		return capteurRepository.findById(id).get();
	}

	@Override
	public Capteur update(Capteur capteur) {
		Capteur c = capteurRepository.findById(capteur.getId()).get();
		c.setActif(capteur.isActif());
		c.setName(capteur.getName());
		return capteurRepository.saveAndFlush(c);
	}

	@Override
	public Capteur add(Capteur capteur) {
		return capteurRepository.save(capteur);
	}

	@Override
	public Capteur getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}

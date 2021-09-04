package com.example.security.model;

import javax.persistence.*;

@Entity
public class RxCapteur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RxCapteur(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public RxCapteur() {
		super();
	}

}

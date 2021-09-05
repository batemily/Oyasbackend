package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "T_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true,nullable = false)
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	@JsonProperty("isActive")
	private boolean isActive=true;
	@Column(unique = true,nullable = false)
	private String tel;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="user_role", joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id")} , 
	inverseJoinColumns = {@JoinColumn(name="role_id",referencedColumnName = "id")})
	private List<Role> roles;
	//private List<RxCapteur> rxCapteurs;

	public User(String email, String firstName, String lastName,  String password, String tel, List<Role> roles , boolean isActive){
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password =password;
		this.roles = roles;
		this.isActive =isActive;
	}

	
	
		
}

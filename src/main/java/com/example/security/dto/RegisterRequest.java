package com.example.security.dto;

//import com.example.security.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String tel;

}

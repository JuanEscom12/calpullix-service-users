package com.calpullix.service.users.model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterUserRequest {

	private Integer id;
	
	private String name;
	
	private String lastName;
	
	private String email;
	
	private BigInteger phone;

}

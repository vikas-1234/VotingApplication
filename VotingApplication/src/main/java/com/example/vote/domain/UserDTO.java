package com.example.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	
	private String userName;
	private String password;
	private String emailId;
	private String phoneNumber;
	private boolean enabled;
	
	
}

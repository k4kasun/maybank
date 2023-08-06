package com.maybank.maybank.app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsRequest {
	
	
	private String userName;
	private String password;
	

}

package com.maybank.maybank.app.rest;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.maybank.app.entity.User;
import com.maybank.maybank.app.request.UserCredentialsRequest;
import com.maybank.maybank.app.service.UserService;


//import com.maybank.maybank.app.service.UserService;

import com.maybank.maybank.app.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService  userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> doLogin(@RequestBody UserCredentialsRequest credentialsRequest) throws JoseException{
		System.out.println(BCrypt.hashpw(credentialsRequest.getPassword(), BCrypt.gensalt()));
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsRequest.getUserName(), credentialsRequest.getPassword()));
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("Authentication failed");
			return ResponseEntity.ok("failed");
			
		}
		User u= userService.getUserByUserName(credentialsRequest.getUserName());
		String token= jwtUtil.generateToken(u.getId(),u.getRole().toString(), credentialsRequest.getUserName());
		log.info(token);
		
		return ResponseEntity.ok(token);
	}
	
	@PostMapping("/User")
	public ResponseEntity<User> createUser(@RequestBody User u){
		
//		u.setPassword(encoder.encode(u.getPassword()));
		String password= BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
		u.setPassword(password);
		log.info("password={}",password);
		
		User newUser= userService.createUser(u);
		  
		log.info("user password= {}",newUser.getPassword()); 
		
		return ResponseEntity.ok(newUser);
		
	}
}

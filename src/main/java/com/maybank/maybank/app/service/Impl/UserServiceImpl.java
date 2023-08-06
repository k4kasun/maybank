package com.maybank.maybank.app.service.Impl;

import java.util.List;
import java.util.Optional;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.maybank.app.dao.UserRepository;
import com.maybank.maybank.app.entity.User;
import com.maybank.maybank.app.service.UserService;
import com.maybank.maybank.app.util.JwtUtil;

//import com.k4kasun.rentme.dao.UserRepository;
//import com.k4kasun.rentme.entity.User;
//import com.k4kasun.rentme.service.UserService;
//import com.k4kasun.rentme.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Override
	public List<User> getUsers() {
		
		return userRepository.findAll();
		
	}
	
	public User findUser(int id) {
		User user= userRepository.findById(id).orElseThrow(()->{
			log.error("user not found for: {}",id);
			throw new RuntimeException("not found");
			
		});
		return user;	
		
	}
	
	@Override
	public User getUserByUserName(String userName) {
		
		return userRepository.findByUserName(userName).get();
	}
	 
	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}
	

}

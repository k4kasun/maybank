package com.maybank.maybank.app.service;

import java.util.List;

import com.maybank.maybank.app.entity.User;

public interface UserService {

	public List<User> getUsers();
	

	public User getUserByUserName(String userName);
	
	public User createUser(User user);
	
}

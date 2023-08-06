package com.maybank.maybank.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.maybank.maybank.app.dao.UserRepository;
import com.maybank.maybank.app.entity.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthUserDetailsService  implements UserDetailsService{
	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user= repository.findByUserName(username)
			.orElseThrow(()-> 
				new UsernameNotFoundException("User name not found")
			);
		
		log.info("found user: {}", user.toString());
		 List<GrantedAuthority> authorityList=new ArrayList<>();
		 authorityList.add(new SimpleGrantedAuthority(user.getRole().toString()));
		 
		 
		org.springframework.security.core.userdetails.User springUser= new org.springframework.security.core.userdetails.User (user.getUserName(), user.getPassword(), authorityList);
		return springUser;
	}

}

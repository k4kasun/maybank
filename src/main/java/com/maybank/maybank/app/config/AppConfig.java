package com.maybank.maybank.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.maybank.maybank.app.service.AuthUserDetailsService;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Configuration
public class AppConfig {

	@Autowired
	private AuthUserDetailsService authUserDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(authUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		System.out.println("Authentication Provider got loaded to IOC");
		return authProvider;

	}

}

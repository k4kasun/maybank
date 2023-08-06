package com.maybank.maybank.app.config;

import java.io.IOException;
//import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maybank.maybank.app.util.JwtUtil;
import com.maybank.maybank.app.entity.JwtData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);

		final String userName;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("No Header detailes found");
			filterChain.doFilter(request, response);
			return;
		}
		final String token = authHeader.substring(7);

		if (token == null) {
			log.info("token not found");
			filterChain.doFilter(request, response);
			return;
		}

		JwtData jwtData = null;
		try {
			jwtData = jwtUtil.validateToken(token);
		} catch (MalformedClaimException e) {
			log.info("MalformedClaimException " + e.getMessage());
			filterChain.doFilter(request, response);
			return;
		} catch (InvalidJwtException e) {
			log.info("InvalidJwtExceptio " + e.getMessage());
			filterChain.doFilter(request, response);
			return;
		}

		List<GrantedAuthority> authority = new ArrayList<>();
		authority.add(new SimpleGrantedAuthority(jwtData.getRole()));
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				jwtData.getUserName(), null, authority);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(request, response);


	}

}

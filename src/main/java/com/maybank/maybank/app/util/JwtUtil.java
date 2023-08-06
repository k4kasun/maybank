package com.maybank.maybank.app.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.maybank.maybank.app.entity.JwtData;

@Component
public class JwtUtil{

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	private static final String JWT_SUBJECT="";
	private static final String JWT_ISSUER="http://localhost:9090/rentme/api";
	private static final String JWT_AUDIENCE="http://localhost:9091/rentme/api/svr";
	
	private Key getKey() {
	return new HmacKey(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
	}
	public String generateToken(int userId, String role, String userName) throws JoseException{
		JwtClaims claims=new JwtClaims();
		claims.setExpirationTimeMinutesInTheFuture(12*60);
		claims.setIssuer(JWT_ISSUER);
		claims.setSubject(JWT_SUBJECT);
		claims.setAudience(JWT_AUDIENCE);
		claims.setClaim("userId", userId);
		claims.setClaim("scope", role);
		claims.setClaim("userName", userName);
		
		
		JsonWebSignature jsonWebSignature=new JsonWebSignature();
		jsonWebSignature.setPayload(claims.toJson());
		jsonWebSignature.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
		jsonWebSignature.setKey(getKey());
		
		return jsonWebSignature.getCompactSerialization();
		
	}
	
	public JwtData validateToken(String jwt) throws InvalidJwtException, MalformedClaimException {
		
		JwtConsumer jwtConsumer=new JwtConsumerBuilder()
				.setRequireExpirationTime()
				.setExpectedSubject(JWT_SUBJECT)
				.setExpectedIssuer(JWT_ISSUER)
				.setExpectedAudience(JWT_AUDIENCE)
				.setVerificationKey(getKey())
				.setRelaxVerificationKeyValidation() 
				.build();
		
	JwtClaims jwtClaims=jwtConsumer.processToClaims(jwt);
	
	
	return	JwtData.builder()
			.role(jwtClaims.getClaimValue("scope", String.class))
			.userId(jwtClaims.getClaimValue("userId", Long.class))
			.userName(jwtClaims.getClaimValue("userName", String.class))
			.build();
			
		
		
	}
}

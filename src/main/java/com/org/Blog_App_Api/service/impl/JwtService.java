package com.org.Blog_App_Api.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.org.Blog_App_Api.ExceptionHandler.JwtTimeExpairedException;
import com.org.Blog_App_Api.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {

	String resToken = "";

	public JwtService() {
		try {

			KeyGenerator instance = KeyGenerator.getInstance("HmacSHA256");
			SecretKey generateKey = instance.generateKey();
			resToken = Base64.getEncoder().encodeToString(generateKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateToken(Users user) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("role", user.getRole());
		map.put("status", user.getUserVerification().isActive());

		return Jwts.builder().claims().add(map).subject(user.getEmail()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 20L * 60 * 60 + 1000)).and().signWith(getKey())
				.compact();
	}

	private Key getKey() {
		byte[] decode = Decoders.BASE64.decode(resToken);
		return Keys.hmacShaKeyFor(decode);
	}

	public String extractUserName(String token) {
		Claims extractAllClaims = extractAllClaims(token);
		return extractAllClaims.getSubject();
	}

	public Claims extractAllClaims(String token) {

		try {
			return Jwts.parser().verifyWith(decryptKey()).build().parseSignedClaims(token).getPayload();
		} catch (SignatureException e) {
			throw new SignatureException("Token Did not Match");

		} catch (JwtTimeExpairedException e) {
			throw new JwtTimeExpairedException("Token Expaired");
		} catch (Exception e) {
			throw e;
		}

	}

	private SecretKey decryptKey() {
		byte[] decode = Decoders.BASE64.decode(resToken);
		return Keys.hmacShaKeyFor(decode);

	}

	public boolean authenticate(String token, UserDetails loadUserByUsername) {
		boolean expaired = extractAllClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));

		if (extractUserName(token).equals(loadUserByUsername.getUsername()) && !expaired) {
			return true;
		}
		return false;
	}

}

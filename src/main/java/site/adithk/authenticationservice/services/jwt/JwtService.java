package site.adithk.authenticationservice.services.jwt;

import java.security.Key;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


public interface JwtService {

	
	public String generateToken(UserDetails userDetails);

	public String createToken(Map<String, Object> claims, String username,boolean isAdmin);

	public Key getSignKey();

    String extractUsername(String token);

	boolean validateToken(String token, UserDetails userDetails);

	boolean isTokenExpired(String token);
} 
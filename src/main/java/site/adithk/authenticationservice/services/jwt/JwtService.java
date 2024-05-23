package site.adithk.authenticationservice.services.jwt;

import java.security.Key;
import java.text.ParseException;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


public interface JwtService {


	
	public String generateToken(UserDetails userDetails);

	public String createToken(Map<String, Object> claims, String username,boolean isAdmin);

    String extractUsername(String token);

	boolean validateToken(String token, UserDetails userDetails);

	boolean isTokenExpired(String token);
} 
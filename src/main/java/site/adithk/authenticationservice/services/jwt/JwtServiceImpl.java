package site.adithk.authenticationservice.services.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

	Instant now= Instant.now();

	private static final long EXPIRATION_TIME=1000*60*60*24*5L;
//	private static final SecretKey key = Jwts.SIG.HS384.key().build();

	private static final String SECRET_KEY="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

	private SecretKey getSignKey(){

		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String generateToken(UserDetails userDetails) {

		Map<String,Object> claims
								=new HashMap<>();
		boolean isAdmin=false;
		
		if(userDetails.getAuthorities()!=null){
			isAdmin=userDetails
					.getAuthorities()
					.stream()
					.anyMatch(authority->authority.getAuthority().equals("ADMIN"));
			
		}
			

		return 
			createToken(claims, userDetails.getUsername(),isAdmin);
	}

	@Override
	public String createToken(Map<String,Object>claims, String username,boolean isAdmin) {
		
		
	return Jwts
			.builder()
			.claims(claims)
			.subject(username)
			.claim("admin",isAdmin)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
			.signWith(getSignKey()).compact();
		
	}



	@Override
	public String extractUsername(String token) throws JwtException {
			JwtParser parser=Jwts.parser().verifyWith(getSignKey()).build();
			Jws<Claims> claims=parser.parseSignedClaims(token);
			return claims.getPayload().getSubject();

	}

	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		boolean isValid=false;
		try{
			isValid=extractUsername(token).equals(userDetails.getUsername())&&!isTokenExpired(token);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return isValid;
	}

	public boolean isTokenExpired(String token) {
		JwtParser parser=Jwts.parser().verifyWith(getSignKey()).build();
		Jws<Claims> claims=parser.parseSignedClaims(token);
		return claims.getPayload().getExpiration().before(new Date(System.currentTimeMillis()));
	}


}

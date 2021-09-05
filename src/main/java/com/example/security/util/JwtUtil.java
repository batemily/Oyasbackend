package com.example.security.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.springframework.security.authorization.AuthorityAuthorizationManager;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Slf4j
public class JwtUtil {

	private static final String SECRET_KEY = "jwtSecretKey";
	private static final long EXPIRATION_DATE = 86400;
	
	public String generateToken(UserDetails user) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		List<String> authoritiesNames = new ArrayList<>();
		for(GrantedAuthority authority:authorities) {
			authoritiesNames.add(authority.getAuthority());
		}
		claims.put("authorities",authoritiesNames);
		claims.put("email", user.getUsername());
		claims.put("exp", new Date(System.currentTimeMillis()+EXPIRATION_DATE*1000));
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
	}
	
	public String extractEmail(String token) {
		Claims claims =  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		String email = (String) claims.get("email");
		return email;
	}
	
	private Date extractExpirationDate(String token) {
		Claims claims =  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		log.info("Date {}",new Date((long)claims.get("exp")));
		return new Date((long)claims.get("exp")) ;
	}
	
	public boolean validateToken(String token, String email) {
		return !extractExpirationDate(token).before(new Date()) && email.equals(extractEmail(token));
	}
}

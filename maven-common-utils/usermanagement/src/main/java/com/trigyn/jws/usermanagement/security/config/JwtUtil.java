package com.trigyn.jws.usermanagement.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.service.PropertyMasterService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private final Log								logger						= LogFactory.getLog(getClass());

	@Autowired
	@Lazy
	private PropertyMasterService	propertyMasterService	= null;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		String secretKey;
		try {
			secretKey = propertyMasterService.findPropertyMasterValue("system", "system",
					"jwsSecretKey");
			if(secretKey !=null)
				return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		} catch (Exception exec) {
			logger.error("Failed : Error while extract " + exec.getMessage());
		}
		return null;
		
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		String secretKey = null;
		try {
			secretKey = propertyMasterService.findPropertyMasterValue("system", "system",
					"jwsSecretKey");
			if(secretKey !=null)
				return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(new Date(System.currentTimeMillis() + 1000 * 90 * 60)) // 150 mins
						.signWith(SignatureAlgorithm.HS256, secretKey).compact();
		} catch (Exception exec) {
			logger.error("Failed : Error while creating token " + exec.getMessage());
		}
		return secretKey;
		
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.toLowerCase().equals(userDetails.getUsername().toLowerCase()) && !isTokenExpired(token));
	}

}

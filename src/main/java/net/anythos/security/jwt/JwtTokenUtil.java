package net.anythos.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.anythos.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {
	
	public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;  //24h
	
	//@Value("${jwt.secret}")
	private static final String secret = "kronos_secret213";
	
	//TODO: fix error: Invalid JWT signature: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
	public String generateToken(User user) {
		
		return Jwts.builder()
				.setSubject(String.format("%s,%s", user.getId(), user.getUsername()))
				.setIssuer("Anythos")
				.claim("roles", user.getRoles().toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	public Boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT token is null or empty: {}", e.getMessage());
		}
		return false;
	}
	
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}
	public String getSubjectFromToken(String token) {           //getting username from token
		return parseClaims(token).getSubject();
	}
	
	//alternative methods to get data from token
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public static Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token.substring(7))
				.getBody();
	}
	
	public String getUsernameFromToken(String token) {
		//return getClaimFromToken(token, Claims::getSubject);
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	
	

	
//	public static String generateToken(String username, Claims claims) {
//		return Jwts.builder()
//				.setHeaderParam("typ", "JWT")
//				.setClaims(claims)
//				.setSubject(username)
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//				.signWith(SignatureAlgorithm.HS512, secret)
//				.compact();
//	}
	
	public static void verifyToken(String token) throws JwtException {
		Jwts.parser()
				.setSigningKey(secret)
				.parse(token.substring(7));
	}
	
	public static String updateExpirationDateToken(String token) {
		Claims claims = getAllClaimsFromToken(token);
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	
//	public Boolean validateToken(String token, UserDetails userDetails) {
//		final String username = getUsernameFromToken(token);
//		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//	}
//
//	private boolean isTokenExpired(String token) {
//		final Date expiration = getExpirationDateFromToken(token);
//		return expiration.before(new Date());
//	}
}

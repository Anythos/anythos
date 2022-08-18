package net.anythos.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.anythos.security.config.Security_Constants.*;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				//.parseClaimsJws(token.substring(7))
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String generateToken(Authentication authentication) {
		final String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
				.compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication auth, final UserDetails userDetails) {
		
		final JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET);
		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
		final Claims claims = claimsJws.getBody();
		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}
	
//	public static String updateExpirationDateToken(String token) {
//		Claims claims = getAllClaimsFromToken(token);
//		return Jwts.builder()
//				.setHeaderParam("typ", "JWT")
//				.setClaims(claims)
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
//				.signWith(SignatureAlgorithm.HS512, secret)
//				.compact();
//	}
//	public Boolean validateToken(String authToken) {
//		try {
//			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
//			return true;
//		} catch (SignatureException e) {
//			log.error("Invalid JWT signature: {}", e.getMessage());
//		} catch (MalformedJwtException e) {
//			log.error("Invalid JWT token: {}", e.getMessage());
//		} catch (ExpiredJwtException e) {
//			log.error("JWT token is expired: {}", e.getMessage());
//		} catch (UnsupportedJwtException e) {
//			log.error("JWT token is unsupported: {}", e.getMessage());
//		} catch (IllegalArgumentException e) {
//			log.error("JWT claims string is empty: {}", e.getMessage());
//		}
//		return false;
//	}

}

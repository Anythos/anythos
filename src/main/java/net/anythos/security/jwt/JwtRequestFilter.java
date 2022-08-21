package net.anythos.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain chain) throws ServletException, IOException {
		
		if (!hasAuthorizationBearer(request)) {
			chain.doFilter(request, response);
			return;
		}
		String token = getJwtToken(request);
		
		if (!jwtTokenUtil.validateToken(token)) {
			chain.doFilter(request, response);
			return;
		}
		setAuthenticationContext(token, request);
		chain.doFilter(request, response);
	}
	
	private UserDetails getUserDetails(String token) {
		User userDetails = new User();
		Claims claims = jwtTokenUtil.parseClaims(token);
		String subject = (String) claims.get(Claims.SUBJECT);
		String roles = (String) claims.get("roles");
		
		System.out.println("SUBJECT: " + subject);
		System.out.println("roles: " + roles);
		
		roles = roles.replace("[", "").replace("]", "");
		String[] roleNames = roles.split(",");
		
		for (String name : roleNames) {
			userDetails.addRole(new Role(name));
		}
		
		String[] jwtSubject = subject.split(",");
		
		userDetails.setId(Long.parseLong(jwtSubject[0]));
		userDetails.setUsername(jwtSubject[1]);
		
		return userDetails;
	}
	
	private void setAuthenticationContext(String token, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(token);
		
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
//	private String parseJwt(HttpServletRequest request) {
//		String requestTokenHeader = request.getHeader("Authorization");
//		if (StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
//			return requestTokenHeader.substring(7, requestTokenHeader.length());
//		}
//		return null;
//	}
	
	private String getJwtToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		return header.substring("Bearer ".length());
		//return header.split(" ")[1].trim();     //return jwtToken from Header
	}
	
	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
	}
}
	

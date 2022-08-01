package net.anythos.security.jwt;

import lombok.extern.slf4j.Slf4j;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public JwtRequestFilter() {
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		try {
			String jwtToken = parseJwt(request);
			
			if (jwtToken != null && jwtTokenUtil.validateToken(jwtToken)) {
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				UserDetails userDetails = userService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		chain.doFilter(request, response);
	}
	private String parseJwt(HttpServletRequest request) {
		String requestTokenHeader = request.getHeader("Authorization");
		if (StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
			return requestTokenHeader.substring(7, requestTokenHeader.length());
		}
		return null;
	}
	
	//getting UserDetails by using SecurityContext
//	UserDetails userDetails =
//			(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	

//		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//			jwtToken = requestTokenHeader.substring(7);
//
//			try {
//				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//			}
//			catch (IllegalArgumentException e) {
//				log.info("Unable to get token.");
//			}
//			catch (ExpiredJwtException e) {
//				log.info("Token has expired.");
//			}
//		}
//		else {
//			log.warn("Token does not start with \"Bearer\"");
//		}
//
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//			UserDetails userDetails = userService.loadUserByUsername(username);
//
//			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =  new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				usernamePasswordAuthenticationToken
//						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//			}
//		}
//		chain.doFilter(request, response);
//	}
}

package net.anythos.authorization.controller;

import lombok.extern.slf4j.Slf4j;
import net.anythos.authorization.model.AuthToken;
import net.anythos.authorization.model.LoginUser;
import net.anythos.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/anythos")
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private RoleRepository roleRepository;
	
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginUser loginUser) throws AuthenticationException {
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		final String token = jwtTokenUtil.generateToken(authentication);
		
		return ResponseEntity.ok(new AuthToken(token));
	}
}

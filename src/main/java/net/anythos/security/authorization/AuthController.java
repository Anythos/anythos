package net.anythos.security.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//	private final AuthService authenticationService;
	
//	@PostMapping("/home")
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest request) throws Exception {
//
//		authenticationService.authenticate(new LoginRequest(request.getUsername(), request.getPassword()));
//
//		final UserDetails userDetails = userDetailsService
//				.loadUserByUsername(authenticationRequest.getUsername());
//
//		final String token = jwtTokenUtil.generateToken(userDetails);
//
//		return ResponseEntity.ok(new JwtResponse(token));

//	@PostMapping("/anythos/login")
//	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
//		log.info("User {} is logging in", request.getUsername());
//		return ResponseEntity.ok(authenticationService.authenticate(request));
//	}
//@PreAuthorize("isAuthenticated() || hasRole('ADMIN')")

	//}
//}

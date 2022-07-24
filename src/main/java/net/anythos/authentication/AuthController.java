package net.anythos.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authenticationService;
	
	@PostMapping("/anythos/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
		log.info("User {} is logging in", request.getUsername());
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}

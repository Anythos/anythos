package net.anythos.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/anythos")
public class UserApi {
	@Autowired
	private UserService userService;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	@Value("${jwt.secret}")
	private String secret;
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String TOKEN_REPLACEMENT = "";
	@GetMapping("/admin/users")
	@RolesAllowed("ROLE_ADMIN")
	public List<User> showAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping("/admin/test")
	@RolesAllowed("ROLE_ADMIN")
	public String adminTest() {
		return "Admin test";
	}
	@GetMapping("/user/test")
	@RolesAllowed("ROLE_USER")
	public String userTest() {
		return "User test";
	}

	@PutMapping("admin/users/add")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
		User newUser = userService.addUser(user);
		URI uri = URI.create("/users/add/" + newUser.getId());
		return ResponseEntity.created(uri).body(newUser);
	}

	@GetMapping("/admin/token")
	@RolesAllowed("ROLE_ADMIN")
	public String testToken(@RequestHeader(AUTHORIZATION_HEADER) String token) {

		String name = JWT.require(Algorithm.HMAC256(secret))
				.build()
				.verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
				.getSubject();
		String payload = JWT.require(Algorithm.HMAC256(secret))
				.build()
				.verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
				.getPayload();

		return name + " " + payload;
	}
}

package net.anythos.user.controller;

import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/anythos")

public class UserApi {
	@Autowired
	private UserService userService;
	
	@GetMapping("/admin/users")
	@RolesAllowed("ROLE_USER")
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
}

package net.anythos.user.controller;

import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	@RolesAllowed("ROLE_ADMIN")
	public List<User> showAllUsers() {
		return userService.findAllUsers();
	}
	@PutMapping("admin/users/add")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
		User newUser = userService.addUser(user);
		URI uri = URI.create("/users/add/" + newUser.getId());
		return ResponseEntity.created(uri).body(newUser);
	}
}

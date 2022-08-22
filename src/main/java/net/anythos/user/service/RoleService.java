package net.anythos.user.service;

import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public void addRoleToUser (User user, String authority) {
		Set <Role> roles = user.getRoles();
		Role role = roleRepository.findRoleByName(authority);
		roles.add(role);
		user.setRoles(roles);
		
		
		
	}
}

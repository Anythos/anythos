package net.anythos.user.service;

import lombok.RequiredArgsConstructor;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final RoleRepository roleRepository;
	
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	public User addUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}


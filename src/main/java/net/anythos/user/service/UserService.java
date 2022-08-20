package net.anythos.user.service;

import lombok.RequiredArgsConstructor;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//	@Override
//	@Transactional
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User Not Found for username: " + username));
//		return UserDetailsImpl.build(user);
//	}
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	public User addUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Transactional
	public void addDefaultUser() {
		User user = new User();
		user.setUsername("bartek");
		user.setPassword(passwordEncoder.encode("bartek"));
		user.setStatus("ACTIVE");
		user.setRoles(Set.of(new Role(null, "USER")));
		userRepository.save(user);
		
	}
}


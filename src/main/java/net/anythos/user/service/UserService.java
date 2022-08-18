package net.anythos.user.service;

import lombok.*;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "userService")
public class UserService implements UserDetailsService{
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder passEncoder;
	
//	public UserService(UserRepository userRepository, BCryptPasswordEncoder passEncoder) {
//		this.userRepository = userRepository;
//		this.passEncoder = passEncoder;
//	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found for username: " + username));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority(user));
	}
	private Set<SimpleGrantedAuthority> getAuthority(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
		// return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	public List<User> findAllUsers() {
		List<User> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	public User findUserById(long id) {
		return userRepository.findById(id);
	}
	public User createUser(String username, String password, String status) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(passEncoder.encode(password));
		user.setStatus(status);
		return userRepository.save(user);
	}
}

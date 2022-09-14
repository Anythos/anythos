package net.anythos.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import net.anythos.employee.repository.EmployeeRepository;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserService {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_REPLACEMENT = "";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final RoleRepository roleRepository;
    @Value("${jwt.secret}")
    private String secret;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }



    private String getName(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getSubject();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException(id.toString()));
    }

    public void save(User user) {
        userRepository.save(user);
    }

}


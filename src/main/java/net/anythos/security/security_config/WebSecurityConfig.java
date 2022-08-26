package net.anythos.security.security_config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.anythos.security.jwt.TokenAuthorizationFilter;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableWebSecurity//(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String EXCEPTION_MESSAGE = "Error while configuring HttpSecurity class, exception message: %s";

    private final AuthenticationFilter authenticationFilter;
    private final DetailsService detailsService;
    @Value("${jwt.secret}")
    private String secret;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    protected void configure(HttpSecurity http) {
        try {
            http.csrf().disable();
            http.cors();
            configureSecurity(http);
        } catch (Exception exception) {
            log.warn(EXCEPTION_MESSAGE.formatted(exception.getMessage()));
        }
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization",  "content-type"));
        configuration.setExposedHeaders(Arrays.asList("authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private void configureSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter)
                .addFilter(new TokenAuthorizationFilter(authenticationManager(), detailsService, secret))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void get() {
        User admin = new User(null, "bartek", passwordEncoder().encode("bartek"), true, null); //Set.of(new Role("ADMIN"), new Role("MOD"))
        User user = new User(null, "user", passwordEncoder().encode("user"), true, null);
//        roleRepository.save(new Role("ADMIN"));
//        roleRepository.save(new Role("USER"));
//        roleRepository.save(new Role("MANAGER"));
        admin.setRoles(Stream.of(roleRepository.findRoleByName("ADMIN")).collect(Collectors.toSet()));
        user.setRoles(Set.of(roleRepository.findRoleByName("USER")));
        admin.addRole(roleRepository.findRoleByName("MANAGER"));

        userRepository.saveAll(List.of(admin, user));

//        ********************************
        //User user1 = new User(null, "user1", passwordEncoder().encode("user1"), true, Set.of(new Role("USER")));
        // System.out.println(admin.getRoles());

//		User user = new User("kamil", passwordEncoder().encode("kamil"), true);
//		userRepository.save(user);
//		roleService.addRoleToUser(user, "ROLE_USER");
//		userRepository.save(user);
//		System.out.println(user.getRoles());
    }
}


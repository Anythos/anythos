package net.anythos.security.security_config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.anythos.security.jwt.TokenAuthorizationFilter;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Configuration
@EnableWebSecurity//(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebSecurityConfig {
    //    private static final String EXCEPTION_MESSAGE = "Error while configuring HttpSecurity class, exception message: %s";
    @Value("${jwt.secret}")
    private String secret;
    private final AuthenticationFilter authenticationFilter;
    private final DetailsService detailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
        configuration.setExposedHeaders(Arrays.asList("authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(authenticationFilter)
                .addFilter(new TokenAuthorizationFilter(new AuthManager(), detailsService, secret))
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPointJwt)
                .and()
                .requestMatchers(requestMatchers -> requestMatchers.antMatchers("/anythos/**"))
                .authorizeRequests(authorizeRequests ->
                                authorizeRequests
//                                .antMatchers( "/anythos/user/**", "/anythos/admin/**").hasRole("ADMIN")
//                                .antMatchers("/anythos/user/**").hasRole("USER")
                                        .antMatchers("/anythos/admin/**").hasRole("ADMIN")
                                        .antMatchers("/anythos/user/**").hasRole("USER")
                );
        //.formLogin().loginPage("/anythos/login").permitAll();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void get() {
        User admin = new User(null, "bartek", passwordEncoder().encode("bartek"), true, null, null); //Set.of(new Role("ADMIN"), new Role("MOD"))
        User user = new User(null, "user", passwordEncoder().encode("user"), true, null, null);
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


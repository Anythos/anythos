package net.anythos.security.security_config;

import lombok.extern.slf4j.Slf4j;
import net.anythos.security.jwt.JwtRequestFilter;
import net.anythos.user.entity.User;
import net.anythos.user.repository.UserRepository;
import net.anythos.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found.")));
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.mvcMatchers("/home").hasRole("USER")
				.antMatchers("/anythos/home/**", "/anythos/employee/**").hasRole("USER")
				.antMatchers("/anythos/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated();
		
//		httpSecurity.formLogin()
//				.loginPage("/login");
//				.defaultSuccessUrl("/anythos/home")
//				.usernameParameter("username")
//				.passwordParameter("password")
//				.failureUrl("/anythos/login?error=true");
		
		httpSecurity.exceptionHandling()
				.authenticationEntryPoint((request, response, e) ->
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()));
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}

	@EventListener(ApplicationReadyEvent.class)
	public void get() {
		User admin = new User("bartek", passwordEncoder().encode("bartek"), "ACTIVE");
		userRepository.save(admin);
		roleService.addRoleToUser(admin, "ROLE_ADMIN");
		userRepository.save(admin);
		System.out.println(admin.getRoles());
		
		User user = new User("kamil", passwordEncoder().encode("kamil"), "ACTIVE");
		userRepository.save(user);
		roleService.addRoleToUser(user, "ROLE_USER");
		userRepository.save(user);
		System.out.println(user.getRoles());
		}
	}


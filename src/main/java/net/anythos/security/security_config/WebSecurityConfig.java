package net.anythos.security.security_config;

import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.anythos.security.jwt.JwtAuthenticationEntryPoint;
//import net.anythos.config.jwt.JwtRequestFilter;
import net.anythos.security.jwt.JwtRequestFilter;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import net.anythos.user.repository.RoleRepository;
import net.anythos.user.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//@PreAuthorize, @PostAuthorize, @RolesAllowed
//@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
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
		httpSecurity.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.mvcMatchers("/anythos/login").permitAll()
				.antMatchers("/anythos/home/**", "/anythos/employee/**").hasRole("USER")
				.antMatchers("/anythos/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated();
		
		httpSecurity.exceptionHandling()
				.authenticationEntryPoint((request, response, e) ->
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()));
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


//		httpSecurity.csrf()
//				.ignoringAntMatchers("/**")
//				.and()
//				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
//				.addFilter(new JwtAuthorizationFilter(authenticationManager()));

//				.formLogin()
//				.loginPage("/anythos/login")
//				.defaultSuccessUrl("/anythos/home").and()
//				.usernameParameter("username")
//				.passwordParameter("password")
//				.failureUrl("/anythos/login?error=true").and()
//
//	}
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void get() {
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findRoleByName("ADMIN"));
		//TODO extract roles from Set
		User user = new User("bartek", passwordEncoder().encode("bartek"), "ACTIVE");
		user.setRoles(roles);
		userRepository.save(user);
		}
	}


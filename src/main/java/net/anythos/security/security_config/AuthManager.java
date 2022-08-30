package net.anythos.security.security_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationManager {

    private static final String MISS_AUTH_PARAMS = "You miss authentication params!";
    private static final String PASSWORD_NOT_MATCH = "Password not match!";
    private static final String BLOCKED = "User is blocked!";

    @Autowired
    private DetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getPrincipal() == null || auth.getCredentials() == null) {
            throw new BadCredentialsException(MISS_AUTH_PARAMS);
        }
        Details details = loadUser(auth);
        //order https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationManager.html
        if (!details.isEnabled()) {
            throw new DisabledException(auth.getPrincipal().toString());
        }
        if (!details.isAccountNonLocked()) {
            throw new LockedException(BLOCKED);
        }
        if (!passwordEncoder.matches(auth.getCredentials().toString(), details.getPassword())) {
            throw new BadCredentialsException(PASSWORD_NOT_MATCH);
        }
        return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
    }

    private Details loadUser(Authentication authentication) {
        return userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
    }
}

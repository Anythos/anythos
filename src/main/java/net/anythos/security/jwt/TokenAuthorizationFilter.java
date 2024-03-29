package net.anythos.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import net.anythos.security.security_config.Details;
import net.anythos.security.security_config.DetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String TOKEN_HEADER_PREFIX = "Bearer ";
    private static final String EMPTY_STRING = "";
    private final DetailsService userDetailsService;
    private final String secret;

    public TokenAuthorizationFilter(AuthenticationManager authenticationManager, DetailsService userDetailsService,
                                    String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        var authentication = getAuthenticationToken(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) throws ServletException {
        var token = request.getHeader(TOKEN_HEADER_NAME);
        if (token != null && token.startsWith(TOKEN_HEADER_PREFIX)) {
            try {
                var userName = getName(token);
                if (userName != null) {
                    Details userDetails = getDetails(userName);
                    return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                            userDetails.getAuthorities());
                }
            } catch (TokenExpiredException ex) {
                request.setAttribute("JwtExpired", ex.getMessage());
                //throw new TokenExpiredException("Token expired on " + ex.getExpiredOn(), ex.getExpiredOn());
            }

        }
        return null;
    }

    private Details getDetails(String userName) {
        return userDetailsService.loadUserByUsername(userName);
    }

    private String getName(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_HEADER_PREFIX, EMPTY_STRING))
                .getSubject();
    }
}
	

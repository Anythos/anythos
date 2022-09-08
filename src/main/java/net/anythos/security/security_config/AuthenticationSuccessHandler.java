package net.anythos.security.security_config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.anythos.security.refreshtoken.RefreshToken;
import net.anythos.security.refreshtoken.RefreshTokenService;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String TOKEN_HEADER_PREFIX = "Bearer %s";
    private static final String REFRESHTOKEN_HEADER_NAME = "RefreshToken";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    private RefreshTokenService refreshTokenService;
    private UserRepository userRepository;

    public AuthenticationSuccessHandler(RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String token = createToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        response.setHeader(TOKEN_HEADER_NAME, TOKEN_HEADER_PREFIX.formatted(token));
        response.setHeader(REFRESHTOKEN_HEADER_NAME, refreshToken.getToken());
    }

    private String createToken(Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        return JWT.create()
                .withSubject(principal.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }
}

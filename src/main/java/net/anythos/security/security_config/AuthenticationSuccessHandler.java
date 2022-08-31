package net.anythos.security.security_config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.anythos.security.refreshtoken.RefreshToken;
import net.anythos.security.refreshtoken.RefreshTokenService;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_VALUE = "Bearer %s";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String token = createToken(authentication);
//        System.out.println(authentication.getCredentials());
//        System.out.println(authentication.getPrincipal());
//        System.out.println(authentication.getName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                userRepository.findByUsername(
                        authentication.getName()).get().getId());
        response.setHeader(HEADER_NAME, HEADER_VALUE.formatted(token));
        //response.setHeader("isRefreshToken", "true");
        response.setHeader("RefreshToken", refreshToken.getToken());
    }

    private String createToken(Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        return JWT.create()
                .withSubject(principal.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }
}

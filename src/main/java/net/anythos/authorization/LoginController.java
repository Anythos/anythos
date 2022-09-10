package net.anythos.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import net.anythos.security.refreshtoken.RefreshToken;
import net.anythos.security.refreshtoken.RefreshTokenRepository;
import net.anythos.security.refreshtoken.RefreshTokenService;
import net.anythos.security.security_config.CredentialsDTO;
import net.anythos.user.entity.User;
import net.anythos.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class LoginController {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String REFRESHTOKEN_HEADER_NAME = "RefreshToken";
    private static final String TOKEN_HEADER_PREFIX = "Bearer %s";
    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;

    public LoginController(RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid CredentialsDTO credentialsDTO,
                                          HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestHeader String refreshToken) {

        HttpHeaders responseHeaders = getNewTokens(refreshToken);
        return ResponseEntity.ok().headers(responseHeaders).body("Response Headers");

    }

    private HttpHeaders getNewTokens(String oldRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new EntityNotFoundException("RefreshToken not found."));

        refreshTokenService.verifyRefreshToken(refreshToken);

        User user = refreshToken.getUser();
        String userName = user.getUsername();
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add(
                TOKEN_HEADER_NAME,
                TOKEN_HEADER_PREFIX.formatted(
                        createToken(userName)));

        String newRefreshToken = refreshTokenService.createRefreshToken(userName).getToken();
        responseHeaders.add(REFRESHTOKEN_HEADER_NAME, newRefreshToken);

        return responseHeaders;
    }

    private String createToken(String userName) {
        return JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }
}

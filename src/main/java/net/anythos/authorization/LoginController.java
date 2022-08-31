package net.anythos.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    private static final String HEADER_VALUE = "Bearer %s";
    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;
    private UserRepository userRepository;

    public LoginController(RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid CredentialsDTO credentialsDTO,
                                          HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestHeader String refreshToken) {

        HttpHeaders responseHeaders = new HttpHeaders();
        RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
        Instant expDate = refreshToken1.getExpiryDate();

        if (Objects.equals(refreshToken1, refreshTokenService.verifyRefreshToken(refreshToken1))) {
            User user = refreshToken1.getUser();
            responseHeaders.add(TOKEN_HEADER_NAME, HEADER_VALUE.formatted(createToken(user.getUsername())));
            String newRefreshToken = refreshTokenService.createRefreshToken(
                    userRepository.findByUsername(
                            user.getUsername()).get().getId()).getToken();
            responseHeaders.add(REFRESHTOKEN_HEADER_NAME, newRefreshToken);
            String token = createToken(user.getUsername());
            return ResponseEntity.ok().headers(responseHeaders).body("Response Headers");
        }


        return null;
    }

    private String createToken(String userName) {
        return JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }
}

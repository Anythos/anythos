package net.anythos.security.refreshtoken;

import com.auth0.jwt.exceptions.TokenExpiredException;
import net.anythos.user.entity.User;
import net.anythos.user.repository.UserRepository;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationDateInMs}")
    private Long refreshTokenDurationMs;
    RefreshTokenRepository refreshTokenRepository;
    UserRepository userRepository;
    UserService userService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public RefreshToken createRefreshToken(String userName) {

        User user = userService.getUserByUsername(userName);
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        user.setRefreshToken(refreshToken);
        userService.save(user);

        return refreshToken;
    }

    //@Transactional
    public RefreshToken verifyRefreshToken(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException(
                    "Refresh token expired. Please signin again to get new one.",
                    token.getExpiryDate());
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}

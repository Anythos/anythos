package net.anythos.security.refreshtoken;

import net.anythos.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUser(User user);

    int deleteByUser(User user);

    boolean existsByUser(User user);
}

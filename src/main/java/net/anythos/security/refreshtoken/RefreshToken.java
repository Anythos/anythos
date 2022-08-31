package net.anythos.security.refreshtoken;

import lombok.Getter;
import lombok.Setter;
import net.anythos.user.entity.User;

import javax.persistence.*;
import java.time.Instant;
@Getter
@Setter
@Entity(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "refreshToken")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;

}

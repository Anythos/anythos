package net.anythos.security.refreshtoken;

import lombok.Getter;
import lombok.Setter;
import net.anythos.user.entity.User;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refreshtoken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;

}

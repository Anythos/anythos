package net.anythos.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/anythos")
public class UserApi {

    @Autowired
    private UserService userService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Value("${jwt.secret}")
    private String secret;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_REPLACEMENT = "";

    @GetMapping("/admin/users")
    public List<User> showAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Admin test";
    }

    @GetMapping("/user/test")
    public String userTest() {
        return "User test";
    }

    @GetMapping("/admin/userinfo")
    public String testToken(@RequestHeader(AUTHORIZATION_HEADER) String token, @AuthenticationPrincipal String user) { //, @AuthenticationPrincipal UsernamePasswordAuthenticationToken user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getPrincipal().toString();
//User user = (User) authentication == null ? null : (User) authentication.getPrincipal();

        String name = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getSubject();
        String payload = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getPayload();
        String token2 = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getToken();
        String signature = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getSignature();
//        user.getPrincipal() + user.getCredentials() + user.getAuthorities() + get

//        return "name " + name + System.lineSeparator() +
//                "payload: " + payload + System.lineSeparator() +
//                "token: " + token2 + System.lineSeparator() +
//                "signature: " + signature;

        return user;
    }

    // test permissions
    @GetMapping("/user/param")
    public String testParam12(@RequestParam String param1, String param2) {
        return "User: " + param1 + " " + param2;
    }

}

package net.anythos.authorization;

import lombok.extern.slf4j.Slf4j;
import net.anythos.security.security_config.CredentialsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/anythos")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid CredentialsDTO credentialsDTO,
                                          HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
}

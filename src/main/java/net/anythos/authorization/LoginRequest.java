package net.anythos.authorization;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
	
	@NotNull
	private String username;
	
	@NotNull
	private String password;
}

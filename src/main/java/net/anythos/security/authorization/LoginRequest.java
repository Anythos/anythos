package net.anythos.security.authorization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class LoginRequest {
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
}

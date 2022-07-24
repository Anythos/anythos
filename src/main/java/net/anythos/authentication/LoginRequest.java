package net.anythos.authentication;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Builder
public class LoginRequest implements Serializable {
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String passwword;
}

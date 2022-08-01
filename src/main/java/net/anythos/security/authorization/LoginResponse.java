package net.anythos.security.authorization;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
	
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String status;
	private List<String> roles;
	
	public LoginResponse(String token, Long id, String username, String status, List<String> roles) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.status = status;
		this.roles = roles;
	}
}

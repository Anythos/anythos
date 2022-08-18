package net.anythos.security.config;

public class Security_Constants {
	public static final long TOKEN_VALIDITY = 5 * 60 * 60;
	public static final String SECRET = "secret444";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}

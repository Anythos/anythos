package net.anythos.security.authorization;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//	private final AuthenticationManager authenticationManager;
//	private final JwtTokenUtil jwtTokenUtil;
//	private final UserService userService;
//
//	public LoginResponse authenticate(LoginRequest request) {
//		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPasswword()));
//		}
//		catch (BadCredentialsException e) {
//			log.info("Login failed for user: {}, bad credential", request.getUsername());
//			throw e;
//		}
//		catch (DisabledException e) {
//			log.info("Disabled user: {} tried to log in", request.getUsername());
//			throw e;
//		}
//		final UserDetails user = userService.loadUserByUsername(request.getUsername());
//		final String jwtToken = jwtTokenUtil.generateToken(user);
//
//		return new LoginResponse(jwtToken);
//	}
//}

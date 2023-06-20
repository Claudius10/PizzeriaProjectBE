package PizzaApp.api.services.token;

import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.utility.jwt.JWTUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	private final AuthenticationManager authManager;
	private final JWTUtils jwtUtils;

	public TokenServiceImpl(AuthenticationManager authManager,
							JWTUtils jwtUtils) {
		this.authManager = authManager;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public AuthDTO create(LoginDTO login) {
		// 1. Authenticate
		Authentication user = authManager.authenticate
				(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

		User theUser = (User) user.getPrincipal();

		// 2. return AuthDTO
		return new AuthDTO.Builder()
				.withUserId(theUser.getId())
				.withUsername(theUser.getUsername())
				.withAccessToken(jwtUtils.createAccessToken(
						theUser.getUsername(), theUser.getAuthorities()))
				.withRefreshToken(jwtUtils.createRefreshToken(
						theUser.getUsername(), theUser.getAuthorities()))
				.build();
	}

	@Override
	public AuthDTO refresh() {
		return null;
	}
}

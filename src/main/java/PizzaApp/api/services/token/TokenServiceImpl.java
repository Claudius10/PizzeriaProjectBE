package PizzaApp.api.services.token;

import PizzaApp.api.entity.user.Token;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.utility.jwt.JWTUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
				.withAccessToken(jwtUtils.createToken(
						theUser.getUsername(),
						theUser.getId(),
						jwtUtils.parseRoles(theUser.getAuthorities()),
						Instant.now().plus(20, ChronoUnit.SECONDS)))
				.withRefreshToken(jwtUtils.createToken(
						theUser.getUsername(),
						theUser.getId(),
						jwtUtils.parseRoles(theUser.getAuthorities()),
						Instant.now().plus(60, ChronoUnit.SECONDS)))
				.build();
	}

	@Override
	public AuthDTO refresh(Token token) {
		// 1. Validate refresh token
		Jwt jwt = jwtUtils.validate(token.getValue());

		// 2. return AuthDTO
		return new AuthDTO.Builder()
				.withAccessToken(jwtUtils.createToken(
						jwt.getClaim("sub"),
						jwt.getClaim("userId"),
						jwt.getClaim("roles"),
						Instant.now().plus(20, ChronoUnit.SECONDS)))
				.withRefreshToken(jwtUtils.createToken(
						jwt.getClaim("sub"),
						jwt.getClaim("userId"),
						jwt.getClaim("roles"),
						Instant.now().plus(20, ChronoUnit.SECONDS)))
				.build();
	}
}
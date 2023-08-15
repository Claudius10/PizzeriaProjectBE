package PizzaApp.api.services.user.auth;

import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.dto.misc.AuthDTO;
import PizzaApp.api.entity.dto.misc.LoginDTO;
import PizzaApp.api.utility.auth.JWTUtils;
import jakarta.servlet.http.Cookie;
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
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authManager;

	private final JWTUtils jwtUtils;

	public AuthServiceImpl(AuthenticationManager authManager, JWTUtils jwtUtils) {
		this.authManager = authManager;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public AuthDTO login(LoginDTO login) {
		// 1. Authenticate
		Authentication auth = authManager.authenticate
				(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

		User user = (User) auth.getPrincipal();
		return new AuthDTO.Builder()
				.withUserId(user.getId())
				.withName(user.getName())
				.withEmail(user.getUsername())
				.withAccessToken(jwtUtils.createToken(
						user.getUsername(),
						user.getId(),
						jwtUtils.parseRoles(user.getAuthorities()),
						Instant.now().plus(1, ChronoUnit.MINUTES)))
				.withRefreshToken(jwtUtils.createToken(
						user.getUsername(),
						user.getId(),
						jwtUtils.parseRoles(user.getAuthorities()),
						Instant.now().plus(2, ChronoUnit.MINUTES)))
				.build();
	}

	@Override
	public AuthDTO refreshTokens(Cookie token) {
		Jwt jwt = jwtUtils.validate(token.getValue());
		return new AuthDTO.Builder()
				.withUserId(jwt.getClaim("id"))
				.withAccessToken(jwtUtils.createToken(
						jwt.getClaim("sub"),
						jwt.getClaim("id"),
						jwt.getClaim("roles"),
						Instant.now().plus(1, ChronoUnit.MINUTES)))
				.withRefreshToken(jwtUtils.createToken(
						jwt.getClaim("sub"),
						jwt.getClaim("id"),
						jwt.getClaim("roles"),
						Instant.now().plus(2, ChronoUnit.MINUTES)))
				.build();
	}
}
package PizzaApp.api.services.user.auth;

import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.dto.misc.AuthDTO;
import PizzaApp.api.entity.dto.misc.LoginDTO;
import PizzaApp.api.exceptions.exceptions.order.ExpiredTokenException;
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
		// TODO - let user know if they try to login with non-existent credentials
		Authentication auth = authManager.authenticate
				(new UsernamePasswordAuthenticationToken(login.email(), login.password()));

		User user = (User) auth.getPrincipal();
		return new AuthDTO(
				user.getId(),
				user.getName(),
				user.getUsername(),
				jwtUtils.createToken(
						Instant.now().plus(1, ChronoUnit.DAYS),
						user.getUsername(),
						user.getId(),
						jwtUtils.parseRoles(user.getAuthorities())),
				jwtUtils.createToken(
						Instant.now().plus(7, ChronoUnit.DAYS),
						user.getUsername(),
						user.getId(),
						jwtUtils.parseRoles(user.getAuthorities())));
	}

	@Override
	public AuthDTO refreshTokens(Cookie token) {
		if (token == null) {
			throw new ExpiredTokenException("Expired refresh token");
		}

		Jwt jwt = jwtUtils.validate(token.getValue());

		return new AuthDTO(
				jwt.getClaim("id"),
				"",
				"",
				jwtUtils.createToken(
						Instant.now().plus(1, ChronoUnit.DAYS),
						jwt.getClaim("sub"),
						jwt.getClaim("id"),
						jwt.getClaim("roles")),
				jwtUtils.createToken(
						Instant.now().plus(7, ChronoUnit.DAYS),
						jwt.getClaim("sub"),
						jwt.getClaim("id"),
						jwt.getClaim("roles"))

		);
	}
}
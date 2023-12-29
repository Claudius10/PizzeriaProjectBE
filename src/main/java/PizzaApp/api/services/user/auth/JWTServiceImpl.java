package PizzaApp.api.services.user.auth;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JWTServiceImpl implements JWTService {

	private final SecurityTokenUtils securityTokenUtils;

	public JWTServiceImpl(SecurityTokenUtils securityTokenUtils) {
		this.securityTokenUtils = securityTokenUtils;
	}

	// TODO - only refresh access token

	@Override
	public String refreshTokens(HttpServletResponse response, Cookie refreshToken) {
		if (refreshToken == null) {
			return "Expired refresh token";
		}

		Jwt jwt = securityTokenUtils.validate(refreshToken.getValue());

		String accessToken = securityTokenUtils.createToken(
				Instant.now().plus(1, ChronoUnit.DAYS),
				jwt.getClaim("sub"),
				jwt.getClaim("id"),
				jwt.getClaim("roles"));

		String newRefreshToken = securityTokenUtils.createToken(
				Instant.now().plus(7, ChronoUnit.DAYS),
				jwt.getClaim("sub"),
				jwt.getClaim("id"),
				jwt.getClaim("roles"));

		SecurityCookieUtils.createAuthCookies(response, accessToken, newRefreshToken, jwt.getClaim("id"));
		return null;
	}
}
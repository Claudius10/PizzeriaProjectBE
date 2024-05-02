package PizzaApp.api.configs.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class SecurityTokenUtils {

	private final JWTUtils jwtUtils;

	public SecurityTokenUtils(JWTUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	// FE domain "https://pizzeriaprojectbe-production.up.railway.app"
	public String createToken(Instant expiry, String username, Long userId, String roles) {
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("https://pizzeriaprojectbe-production.up.railway.app")
				.issuedAt(Instant.now())
				.expiresAt(expiry)
				.subject(username)
				.claim("userId", userId)
				.claim("roles", roles)
				.build();
		return jwtUtils.jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	public void refreshTokens(HttpServletResponse response, Cookie refreshToken) {
		Jwt jwt = validate(refreshToken.getValue());

		String accessToken = createToken(
				Instant.now().plus(1, ChronoUnit.DAYS),
				jwt.getClaim("sub"),
				jwt.getClaim("userId"),
				jwt.getClaim("roles"));

		String newRefreshToken = createToken(
				Instant.now().plus(7, ChronoUnit.DAYS),
				jwt.getClaim("sub"),
				jwt.getClaim("userId"),
				jwt.getClaim("roles"));

		SecurityCookieUtils.createAuthCookies(response, accessToken, newRefreshToken, jwt.getClaim("userId"));
	}

	public Jwt validate(String refreshToken) {
		return jwtUtils.jwtDecoder().decode(refreshToken);
	}

	public String parseAuthorities(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
	}
}

package org.pizzeria.api.configs.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.pizzeria.api.configs.security.utils.SecurityTokenUtils;
import org.pizzeria.api.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ValidLoginHandler implements AuthenticationSuccessHandler {

	private final SecurityTokenUtils securityTokenUtils;

	public ValidLoginHandler(SecurityTokenUtils securityTokenUtils) {
		this.securityTokenUtils = securityTokenUtils;
	}

	@Override
	public void onAuthenticationSuccess
			(HttpServletRequest request,
			 HttpServletResponse response,
			 Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		String accessToken = securityTokenUtils.createToken(
				Instant.now().plus(1, ChronoUnit.DAYS),
				user.getUsername(),
				user.getId(),
				securityTokenUtils.parseAuthorities(user.getAuthorities()));

		String refreshToken = securityTokenUtils.createToken(
				Instant.now().plus(7, ChronoUnit.DAYS),
				user.getUsername(),
				user.getId(),
				securityTokenUtils.parseAuthorities(user.getAuthorities()));

		SecurityCookieUtils.createAuthCookies(response, accessToken, refreshToken, user.getId());
	}
}

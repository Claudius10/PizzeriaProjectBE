package org.pizzeria.api.configs.security.access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pizzeria.api.configs.security.auth.JWTTokenManager;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class ValidLoginHandler implements AuthenticationSuccessHandler {

	private final JWTTokenManager jwtTokenManager;

	public ValidLoginHandler(JWTTokenManager jwtTokenManager) {
		this.jwtTokenManager = jwtTokenManager;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		String accessToken = jwtTokenManager.getAccessToken(user.getEmail(), user.getRoles(), String.valueOf(user.getId()));
		SecurityCookieUtils.serveCookies(response, accessToken);
	}
}
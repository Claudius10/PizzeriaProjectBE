package org.pizzeria.api.configs.security.access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class ClearCookiesLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SecurityCookieUtils.eatAllCookies(request, response);
	}
}
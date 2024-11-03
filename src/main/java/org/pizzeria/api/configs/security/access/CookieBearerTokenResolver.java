package org.pizzeria.api.configs.security.access;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.pizzeria.api.utils.globals.Constants;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public final class CookieBearerTokenResolver implements BearerTokenResolver {

	@Override
	public String resolve(HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, Constants.TOKEN_COOKIE_NAME);
		if (accessToken != null) {
			return accessToken.getValue();
		}
		return null;
	}
}
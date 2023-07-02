package PizzaApp.api.configs.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
final class CookieBearerTokenResolver implements BearerTokenResolver {

	@Override
	public String resolve(HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, "fight");
		if (accessToken != null) {
			return accessToken.getValue();
		}
		return null;
	}
}

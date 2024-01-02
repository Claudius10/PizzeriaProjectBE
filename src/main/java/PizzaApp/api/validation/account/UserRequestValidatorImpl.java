package PizzaApp.api.validation.account;

import PizzaApp.api.configs.security.utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class UserRequestValidatorImpl implements UserRequestValidator {

	private final JWTUtils jwtUtils;

	public UserRequestValidatorImpl(JWTUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Override
	public void validate(Long userId, HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "fight");

		if (cookie == null || !userId.equals(jwtUtils.jwtDecoder().decode(cookie.getValue()).getClaim("id"))) {
			throw new AccessDeniedException("Access denied");
		}
	}
}

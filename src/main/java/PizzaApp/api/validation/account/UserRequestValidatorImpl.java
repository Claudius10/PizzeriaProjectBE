package PizzaApp.api.validation.account;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class UserRequestValidatorImpl implements UserRequestValidator {

	private final JwtDecoder jwtDecoder;

	public UserRequestValidatorImpl(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
	}

	// TODO: refactor this method's functionality using aop

	@Override
	public void validate(String userId, HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "fight");

		if (cookie == null || !Long.valueOf(userId).equals(jwtDecoder.decode(cookie.getValue()).getClaim("id"))) {
			throw new AccessDeniedException("Access denied");
		}
	}
}

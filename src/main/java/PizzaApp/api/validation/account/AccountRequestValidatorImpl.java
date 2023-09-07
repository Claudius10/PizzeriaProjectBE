package PizzaApp.api.validation.account;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class AccountRequestValidatorImpl implements AccountRequestValidator {

	private final JwtDecoder jwtDecoder;

	public AccountRequestValidatorImpl(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
	}

	// TODO: refactor this method's functionality using aop

	@Override
	public void validate(String id, HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "fight");

		if (cookie != null) {
			Jwt validatedJwt = jwtDecoder.decode(cookie.getValue());

			if (!Long.valueOf(id).equals(validatedJwt.getClaim("id"))) {
				throw new AccessDeniedException("Access denied");
			}
		} else {
			throw new AccessDeniedException("Access denied");
		}
	}
}

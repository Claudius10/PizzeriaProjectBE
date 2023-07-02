package PizzaApp.api.utility.auth;

import PizzaApp.api.entity.dto.misc.AuthDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public final class CookieUtils {

	private CookieUtils() {
	}

	public static ResponseCookie cookieCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		return ResponseCookie.from(name, value)
				.path("/")
				.maxAge(maxAge)
				.httpOnly(httpOnly)
				.secure(secure)
				.sameSite("Lax")
				.build();
	}

	public static Cookie cookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(secure);
		return cookie;
	}

	// 24 * 60 * 60 24h
	// 168 * 60 * 60 7 days
	public static void bakeCookies(HttpServletResponse response, AuthDTO auth) {
		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieCookie("fight",
						auth.getAccessToken(),
						20,
						true,
						false) // true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieCookie("me",
						auth.getRefreshToken(),
						120,
						true,
						false)
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieCookie("id",
						String.valueOf(auth.getUserId()),
						168 * 60 * 60,
						false,
						false)
						.toString());
	}

	public static void eatAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
	}
}

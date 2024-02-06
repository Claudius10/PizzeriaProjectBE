package PizzaApp.api.configs.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public final class SecurityCookieUtils {

	private SecurityCookieUtils() {
	}

	public static ResponseCookie bakeCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		return ResponseCookie.from(name, value)
				.path("/")
				.maxAge(maxAge)
				.httpOnly(httpOnly)
				.secure(secure)
				.sameSite("Lax")
				.domain("up.railway.app") // NOTE - on for prod fe
				.build();
	}

	public static Cookie makeCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(secure);
		return cookie;
	}

	// 24 * 60 * 60 24h
	// 168 * 60 * 60 7 days
	public static void createAuthCookies(HttpServletResponse response, String accessToken, String refreshToken, Long userId) {
		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("fight", accessToken,
						24 * 60 * 60,
						true,
						true) // NOTE - true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("pseudo_fight", "exp_d",
						24 * 60 * 60,
						false,
						true) // NOTE - true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("me", refreshToken,
						168 * 60 * 60,
						true,
						true) // NOTE - true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("pseudo_me", "exp_d",
						168 * 60 * 60,
						false,
						true) // NOTE - true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("id", String.valueOf(userId),
						168 * 60 * 60,
						false,
						true) // NOTE - true for prod
						.toString());
	}

	public static void eatAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				cookie.setSecure(true); // NOTE - on for prod fe
				cookie.setDomain(".up.railway.app"); // NOTE - on for prod fe
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
	}
}

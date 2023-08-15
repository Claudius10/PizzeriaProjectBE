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

	public static ResponseCookie bakeCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		return ResponseCookie.from(name, value)
				.path("/")
				.maxAge(maxAge)
				.httpOnly(httpOnly)
				.secure(secure)
				.sameSite("Lax")
				//.domain(".up.railway.app") // for prod fe
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
	public static void newCookies(HttpServletResponse response, AuthDTO auth) {
		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("fight", auth.getAccessToken(),
						120,
						true,
						false) // true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("pseudo_fight", "exp_d",
						120,
						false,
						false) // true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("me", auth.getRefreshToken(),
						180,
						true,
						false)
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("pseudo_me", "exp_d",
						180,
						false,
						false) // true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie("id", String.valueOf(auth.getUserId()),
						180,
						false,
						false)
						.toString());
	}

	public static void eatAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				//cookie.setSecure(true); // for prod fe
				//cookie.setDomain("up.railway.app"); // for prod fe
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
	}
}

package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.services.token.TokenService;
import PizzaApp.api.utility.auth.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/auth")
public class TokenController {

	private final TokenService tokenService;
	private final CookieUtils cookieUtils;

	public TokenController(TokenService tokenService, CookieUtils cookieUtils) {
		this.tokenService = tokenService;
		this.cookieUtils = cookieUtils;
	}

	@PostMapping("/login")
	public ResponseEntity<?> createToken(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		return getResponseEntity(response, tokenService.create(loginDTO));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshToken = WebUtils.getCookie(request, "me");
		assert refreshToken != null;
		return getResponseEntity(response, tokenService.refresh(refreshToken.getValue()));
	}

	private ResponseEntity<?> getResponseEntity(HttpServletResponse response, AuthDTO auth) {
		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieUtils.cookieCookie("fight",
								auth.getAccessToken(),
								24 * 60 * 60,
								true,
								false) // true for prod
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieUtils.cookieCookie("me",
								auth.getRefreshToken(),
								168 * 60 * 60,
								true,
								false)
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieUtils.cookieCookie("username",
								auth.getUsername(),
								168 * 60 * 60,
								false,
								false)
						.toString());

		response.addHeader(HttpHeaders.SET_COOKIE,
				cookieUtils.cookieCookie("userId",
								String.valueOf(auth.getUserId()),
								168 * 60 * 60,
								false,
								false)
						.toString());

		return ResponseEntity.ok().build();
	}
}

package PizzaApp.api.controllers.open;

import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/token")
public class TokenController {

	private final SecurityTokenUtils securityTokenUtils;

	public TokenController(SecurityTokenUtils securityTokenUtils) {
		this.securityTokenUtils = securityTokenUtils;
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletResponse response, HttpServletRequest request) {
		Cookie userIdCookie = WebUtils.getCookie(request, "id");

		if (userIdCookie == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.UNAUTHORIZED.value())
							.withPath(request.getServletPath())
							.withErrorMsg("Access denied: unable to verify user identity")
							.build());
		}

		Cookie refreshToken = WebUtils.getCookie(request, "me");

		if (refreshToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.UNAUTHORIZED.value())
							.withPath(request.getServletPath())
							.withErrorMsg("Access denied: no refresh token present")
							.build());
		}

		Jwt validatedRefreshToken = securityTokenUtils.validate(refreshToken.getValue());

		if (!userIdCookie.getValue().matches(validatedRefreshToken.getClaimAsString("userId"))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.UNAUTHORIZED.value())
							.withPath(request.getServletPath())
							.withErrorMsg("Access denied: fraudulent request")
							.build());
		}

		securityTokenUtils.refreshTokens(response, refreshToken);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

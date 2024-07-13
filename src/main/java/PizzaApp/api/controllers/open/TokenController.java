package PizzaApp.api.controllers.open;

import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.utils.globals.SecurityResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/token")
public class TokenController {

	private final SecurityTokenUtils securityTokenUtils;

	public TokenController(SecurityTokenUtils securityTokenUtils) {
		this.securityTokenUtils = securityTokenUtils;
	}

	@PostMapping("/refresh")
	public ResponseEntity<HttpStatus> refreshTokens(HttpServletResponse response, HttpServletRequest request) {
		Cookie refreshToken = WebUtils.getCookie(request, "me");

		if (refreshToken == null) {
			throw new AccessDeniedException(SecurityResponses.MISSING_TOKEN);
		}

		securityTokenUtils.refreshTokens(response, refreshToken);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
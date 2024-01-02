package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
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
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
		String isValid = securityTokenUtils.refreshTokens(response, WebUtils.getCookie(request, "me"));
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
	}
}

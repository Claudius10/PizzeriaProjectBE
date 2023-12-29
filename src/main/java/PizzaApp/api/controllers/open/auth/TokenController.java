package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.services.user.auth.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/token")
public class TokenController {

	private final JWTService jwtService;

	public TokenController(JWTService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
		String isValid = jwtService.refreshTokens(response, WebUtils.getCookie(request, "me"));
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
	}
}

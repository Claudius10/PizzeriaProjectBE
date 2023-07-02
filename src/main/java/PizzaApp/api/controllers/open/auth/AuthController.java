package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.dto.misc.LoginDTO;
import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.services.user.account.AnonAccService;
import PizzaApp.api.services.user.auth.AuthService;
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
public class AuthController {

	private final AnonAccService anonAccService;
	private final AuthService authService;

	public AuthController(AnonAccService anonAccService, AuthService authService) {
		this.anonAccService = anonAccService;
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
		anonAccService.create(registerDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		CookieUtils.bakeCookies(response, authService.login(loginDTO));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<?> deleteTokens(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshToken = WebUtils.getCookie(request, "me");
		assert refreshToken != null;
		CookieUtils.bakeCookies(response, authService.refreshTokens(refreshToken.getValue()));
		return ResponseEntity.ok().build();
	}
}

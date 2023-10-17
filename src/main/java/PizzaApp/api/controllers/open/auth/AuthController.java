package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.dto.misc.AuthDTO;
import PizzaApp.api.entity.dto.misc.LoginDTO;
import PizzaApp.api.entity.dto.user.UserDTO;
import PizzaApp.api.services.user.auth.AuthService;
import PizzaApp.api.utility.auth.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/csrf")
	public void csrf(HttpServletResponse response, CsrfToken csrfToken) {
		CookieUtils.loadCsrf(response, csrfToken);
	}

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		AuthDTO authDTO = authService.login(loginDTO);
		CookieUtils.newCookies(response, authDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new UserDTO(authDTO.userId(), authDTO.name(), authDTO.email()));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshToken = WebUtils.getCookie(request, "me");
		CookieUtils.newCookies(response, authService.refreshTokens(refreshToken));
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

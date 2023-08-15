package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.dto.misc.AuthDTO;
import PizzaApp.api.entity.dto.misc.LoginDTO;
import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.UserDataDTO;
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
	public ResponseEntity<UserDataDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		AuthDTO authDTO = authService.login(loginDTO);
		CookieUtils.newCookies(response, authDTO);
		return ResponseEntity.ok().body(new UserDataDTO(authDTO.getUserId(), authDTO.getName(), authDTO.getEmail()));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> deleteTokens(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshToken = WebUtils.getCookie(request, "me");
		CookieUtils.newCookies(response, authService.refreshTokens(refreshToken));
		return ResponseEntity.ok().build();
	}
}

package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.services.auth.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class TokenController {

	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public AuthDTO createToken(LoginDTO loginDTO) {
		return null;
	}

	@PostMapping("/refresh")
	public AuthDTO refreshToken() {
		return null;
	}
}

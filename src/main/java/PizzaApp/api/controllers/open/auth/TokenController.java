package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.user.Token;
import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.services.token.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<AuthDTO> createToken(@Valid @RequestBody LoginDTO loginDTO) {
		return ResponseEntity.ok().body(tokenService.create(loginDTO));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthDTO> refreshToken(@RequestBody Token token) {
		return ResponseEntity.ok().body(tokenService.refresh(token));
	}
}

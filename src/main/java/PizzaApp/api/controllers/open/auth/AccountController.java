package PizzaApp.api.controllers.open.auth;

import PizzaApp.api.entity.user.dto.RegisterDTO;
import PizzaApp.api.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/account")
public class AccountController {

	private final UserService userService;

	public AccountController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
		userService.create(registerDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.EmailDTO;
import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.exceptions.validation.account.AccountRequestValidator;
import PizzaApp.api.services.user.account.AccountService;
import PizzaApp.api.utility.auth.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	private final AccountService accountService;

	private final AccountRequestValidator accountRequestValidator;

	public AccountController(AccountService accountService, AccountRequestValidator accountRequestValidator) {
		this.accountService = accountService;
		this.accountRequestValidator = accountRequestValidator;
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDataDTO> findDataById(@PathVariable String id, HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		return ResponseEntity.ok().body(accountService.findDataById(id));
	}

	@PutMapping("/{id}/email")
	public ResponseEntity<?> updateEmail(
			@Pattern(regexp = "^[0-9]{1,10}$", message = "Id: mín 1 digito, máx 10 digitos")
			@PathVariable String id,
			@Valid @RequestBody EmailDTO emailDTO,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		accountService.updateEmail(id, emailDTO.getEmail());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(
			@Pattern(regexp = "^[0-9]{1,10}$", message = "Id: mín 1 digito, máx 10 digitos")
			@PathVariable String id,
			@Valid @RequestBody PasswordDTO passwordDTO,
			HttpServletRequest request,
			HttpServletResponse response) {
		accountRequestValidator.validate(id, request);
		accountService.updatePassword(id, passwordDTO.getPassword());
		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/tel")
	public ResponseEntity<?> updateTel() {
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/address")
	public ResponseEntity<?> updateAddress() {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete() {
		return ResponseEntity.ok().build();
	}

	@GetMapping("/test")
	public ResponseEntity<?> testEndpoint() {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/test")
	public ResponseEntity<?> testPostEndpoint() {
		return ResponseEntity.ok().build();
	}
}




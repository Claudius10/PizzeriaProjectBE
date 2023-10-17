package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.services.user.account.UserService;
import PizzaApp.api.utility.auth.CookieUtils;
import PizzaApp.api.validation.account.UserRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserAccountController {

	private final UserService userService;

	private final UserRequestValidator userRequestValidator;

	public UserAccountController
			(UserService userService,
			 UserRequestValidator userRequestValidator) {
		this.userService = userService;
		this.userRequestValidator = userRequestValidator;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> findUserById
			(@PathVariable Long userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.OK).body(userService.findDTOById(userId));
	}

	@PutMapping("/{userId}/name")
	public ResponseEntity<?> updateName
			(@PathVariable Long userId,
			 @Valid @RequestBody NameChangeDTO nameChangeDTO,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userService.updateName(userId, nameChangeDTO);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{userId}/email")
	public ResponseEntity<?> updateEmail
			(@PathVariable Long userId,
			 @Valid @RequestBody EmailChangeDTO emailChangeDTO,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userService.updateEmail(userId, emailChangeDTO);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<?> updatePassword
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {

		userRequestValidator.validate(userId, request);
		userService.updatePassword(userId, passwordChangeDTO);

		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {

		userRequestValidator.validate(userId, request);
		userService.delete(userId, passwordDTO);

		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}

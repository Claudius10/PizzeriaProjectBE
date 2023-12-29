package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.services.user.account.UserService;
import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
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

	public UserAccountController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findDTOById(userId));
	}

	@PutMapping("/{userId}/update/name")
	public ResponseEntity<?> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {
		String isValid = userService.updateName(userId, nameChangeDTO);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
	}

	@PutMapping("/{userId}/update/email")
	public ResponseEntity<?> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailChangeDTO emailChangeDTO) {
		String isValid = userService.updateEmail(userId, emailChangeDTO);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
	}

	@PutMapping("/{userId}/update/password")
	public ResponseEntity<?> updatePassword
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		String isValid = userService.updatePassword(userId, passwordChangeDTO);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			SecurityCookieUtils.eatAllCookies(request, response);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		String isValid = userService.delete(userId, passwordDTO);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			SecurityCookieUtils.eatAllCookies(request, response);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
	}
}

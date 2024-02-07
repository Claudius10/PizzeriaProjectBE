package PizzaApp.api.controllers.locked;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.services.user.UserService;
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
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findDTOById(userId));
	}

	@PutMapping("/{userId}/name")
	public ResponseEntity<?> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {
		userService.updateName(nameChangeDTO.password(), userId, nameChangeDTO.name());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{userId}/email")
	public ResponseEntity<?> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailChangeDTO emailChangeDTO) {
		userService.updateEmail(emailChangeDTO.password(), userId, emailChangeDTO.email());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<?> updatePassword
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		userService.updatePassword(passwordChangeDTO.currentPassword(), userId, passwordChangeDTO.newPassword());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		userService.delete(passwordDTO.password(), userId);
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}

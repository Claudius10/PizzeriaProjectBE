package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.aop.annotations.ValidateUserId;
import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.user.dto.*;
import PizzaApp.api.services.user.UserService;
import PizzaApp.api.utils.globals.SecurityResponses;
import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@ValidateUserId
	@GetMapping("/csrf")
	public void csrf(HttpServletResponse response, CsrfToken csrfToken) {
		SecurityCookieUtils.loadCsrf(response, csrfToken);
	}

	@ValidateUserId
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Long userId, HttpServletRequest request) {
		Optional<UserDTO> user = userService.findUserDTOById(userId);
		return user
				.map(userDTO -> ResponseEntity.ok().body(userDTO))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@ValidateUserId
	@GetMapping("/{userId}/address")
	public ResponseEntity<Set<Address>> findUserAddressListById(@PathVariable Long userId, HttpServletRequest request) {
		Set<Address> userAddressList = userService.findUserAddressListById(userId);

		if (userAddressList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(userAddressList);
	}

	@ValidateUserId
	@PostMapping("/{userId}/address")
	public ResponseEntity<String> createUserAddress(@PathVariable Long userId, @RequestBody @Valid Address address, HttpServletRequest request) {
		String result = userService.addUserAddress(userId, address);
		String userNotFound = String.format(SecurityResponses.USER_NOT_FOUND, userId);

		if (result != null) {
			if (result.equals(ValidationResponses.ADDRESS_MAX_SIZE)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidationResponses.ADDRESS_MAX_SIZE);
			}

			if (result.equals(userNotFound)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
			}
		}

		return ResponseEntity.ok().build();
	}

	@ValidateUserId
	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<String> deleteUserAddress(@PathVariable Long userId, @PathVariable Long addressId, HttpServletRequest request) {
		String result = userService.removeUserAddress(userId, addressId);
		String userNotFound = String.format(SecurityResponses.USER_NOT_FOUND, userId);

		if (result != null) {
			if (result.equals(ValidationResponses.ADDRESS_NOT_FOUND)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ValidationResponses.ADDRESS_NOT_FOUND);
			}

			if (result.equals(userNotFound)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
			}
		}

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{userId}/name")
	public ResponseEntity<?> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {
		userService.updateUserName(nameChangeDTO.password(), userId, nameChangeDTO.name());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/{userId}/email")
	public ResponseEntity<?> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailChangeDTO emailChangeDTO,
										 HttpServletRequest request, HttpServletResponse response) {
		userService.updateUserEmail(emailChangeDTO.password(), userId, emailChangeDTO.email());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/{userId}/contact_number")
	public ResponseEntity<?> updateContactNumber(@PathVariable Long userId, @Valid @RequestBody ContactNumberChangeDTO contactNumberChangeDTO) {
		userService.updateUserContactNumber(contactNumberChangeDTO.password(), userId, contactNumberChangeDTO.contactNumber());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<?> updatePassword
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		userService.updateUserPassword(passwordChangeDTO.currentPassword(), userId, passwordChangeDTO.newPassword());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		userService.deleteUserById(passwordDTO.password(), userId);
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
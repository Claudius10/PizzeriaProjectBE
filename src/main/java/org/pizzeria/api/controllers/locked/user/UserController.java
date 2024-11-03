package org.pizzeria.api.controllers.locked.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.pizzeria.api.aop.annotations.ValidateUserId;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.user.dto.*;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.utils.globals.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@GetMapping("/{userId}")
	public ResponseEntity<Object> findUserById(@PathVariable Long userId, HttpServletRequest request) {
		Optional<UserDTO> user = userService.findUserDTOById(userId);
		return user.<ResponseEntity<Object>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.accepted().body(String.format(ApiResponses.USER_NOT_FOUND, userId)));
	}

	@ValidateUserId
	@GetMapping("/{userId}/address")
	public ResponseEntity<Object> findUserAddressListById(@PathVariable Long userId, HttpServletRequest request) {
		Set<Address> userAddressList = userService.findUserAddressListById(userId);

		if (userAddressList.isEmpty()) {
			return ResponseEntity.accepted().body(ApiResponses.ADDRESS_LIST_EMPTY);
		}

		return ResponseEntity.ok(userAddressList);
	}

	@ValidateUserId
	@PostMapping("/{userId}/address")
	public ResponseEntity<String> createUserAddress(@RequestBody @Valid Address address, @PathVariable Long userId, HttpServletRequest request) {
		String result = userService.addUserAddress(userId, address);

		if (result != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@ValidateUserId
	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<String> deleteUserAddress(@PathVariable Long addressId, @PathVariable Long userId, HttpServletRequest request) {
		String result = userService.removeUserAddress(userId, addressId);

		if (result != null) {
			return ResponseEntity.accepted().body(result);
		}

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{userId}/name")
	public ResponseEntity<HttpStatus> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {
		userService.updateUserName(nameChangeDTO.password(), userId, nameChangeDTO.name());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{userId}/email")
	public ResponseEntity<HttpStatus> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailChangeDTO emailChangeDTO,
												  HttpServletRequest request, HttpServletResponse response) {
		userService.updateUserEmail(emailChangeDTO.password(), userId, emailChangeDTO.email());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{userId}/contact_number")
	public ResponseEntity<HttpStatus> updateContactNumber(@PathVariable Long userId,
														  @Valid @RequestBody ContactNumberChangeDTO contactNumberChangeDTO) {
		userService.updateUserContactNumber(contactNumberChangeDTO.password(), userId, contactNumberChangeDTO.contactNumber());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<HttpStatus> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
													 HttpServletRequest request, HttpServletResponse response) {
		userService.updateUserPassword(passwordChangeDTO.currentPassword(), userId, passwordChangeDTO.newPassword());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId, @Valid @RequestBody PasswordDTO passwordDTO,
												 HttpServletRequest request, HttpServletResponse response) {
		userService.deleteUserById(passwordDTO.password(), userId);
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.ok().build();
	}
}
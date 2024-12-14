package org.pizzeria.api.web.controllers.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.pizzeria.api.configs.web.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.web.aop.annotations.ValidateUserId;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.user.dto.*;
import org.pizzeria.api.web.globals.ApiResponses;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.USER_BASE)
@Validated
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@ValidateUserId
	@GetMapping(ApiRoutes.USER_ID)
	public ResponseEntity<Response> findUserById(@PathVariable Long userId, HttpServletRequest request) {

		Optional<UserDTO> user = userService.findUserDTOById(userId);

		Response response = Response.builder()
				.statusDescription(user.isPresent() ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(user.isPresent() ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(user.isPresent() ? null : ApiResponses.USER_NOT_FOUND)
				.errorMessage(user.isPresent() ? null : String.valueOf(userId))
				.payload(user.orElse(null))
				.build();

		return ResponseEntity.ok(response);
	}

	@ValidateUserId
	@GetMapping(ApiRoutes.USER_ID + ApiRoutes.USER_ADDRESS)
	public ResponseEntity<Response> findUserAddressListById(@PathVariable Long userId, HttpServletRequest request) {

		Set<Address> userAddressList = userService.findUserAddressListById(userId);

		Response response = Response.builder()
				.statusDescription(!userAddressList.isEmpty() ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(!userAddressList.isEmpty() ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(!userAddressList.isEmpty() ? null : ApiResponses.ADDRESS_LIST_EMPTY)
				.payload(!userAddressList.isEmpty() ? userAddressList : null)
				.build();

		return ResponseEntity.ok(response);
	}

	@ValidateUserId
	@PostMapping(ApiRoutes.USER_ID + ApiRoutes.USER_ADDRESS)
	public ResponseEntity<Response> createUserAddress(@RequestBody @Valid Address address, @PathVariable Long userId, HttpServletRequest request) {

		boolean result = userService.addUserAddress(userId, address);

		Response response = Response.builder()
				.statusDescription(result ? HttpStatus.CREATED.name() : HttpStatus.BAD_REQUEST.name())
				.statusCode(result ? HttpStatus.CREATED.value() : HttpStatus.BAD_REQUEST.value())
				.errorClass(result ? null : ApiResponses.ADDRESS_MAX_SIZE)
				.build();

		return ResponseEntity.status(result ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
	}

	@ValidateUserId
	@DeleteMapping(ApiRoutes.USER_ID + ApiRoutes.USER_ADDRESS + ApiRoutes.USER_ADDRESS_ID)
	public ResponseEntity<Response> deleteUserAddress(@PathVariable Long addressId, @PathVariable Long userId, HttpServletRequest request) {

		boolean result = userService.removeUserAddress(userId, addressId);

		Response response = Response.builder()
				.statusDescription(result ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(result ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(result ? null : ApiResponses.ADDRESS_NOT_FOUND)
				.build();

		return ResponseEntity.ok(response);
	}

	@PutMapping(ApiRoutes.USER_ID + ApiRoutes.USER_NAME)
	public ResponseEntity<Response> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {

		userService.updateUserName(nameChangeDTO.password(), userId, nameChangeDTO.name());

		Response response = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.build();

		return ResponseEntity.ok(response);
	}

	@PutMapping(ApiRoutes.USER_ID + ApiRoutes.USER_EMAIL)
	public ResponseEntity<Response> updateEmail(
			@PathVariable Long userId,
			@Valid @RequestBody EmailChangeDTO emailChangeDTO,
			HttpServletRequest request,
			HttpServletResponse response) {

		userService.updateUserEmail(emailChangeDTO.password(), userId, emailChangeDTO.email());

		Response responseObj = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.build();

		SecurityCookieUtils.eatAllCookies(request, response);

		return ResponseEntity.ok(responseObj);
	}

	@PutMapping(ApiRoutes.USER_ID + ApiRoutes.USER_NUMBER)
	public ResponseEntity<Response> updateContactNumber(
			@PathVariable Long userId,
			@Valid @RequestBody ContactNumberChangeDTO contactNumberChangeDTO) {

		userService.updateUserContactNumber(contactNumberChangeDTO.password(), userId, contactNumberChangeDTO.contactNumber());

		Response responseObj = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.build();

		return ResponseEntity.ok(responseObj);
	}

	@PutMapping(ApiRoutes.USER_ID + ApiRoutes.USER_PASSWORD)
	public ResponseEntity<Response> updatePassword(
			@PathVariable Long userId,
			@Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			HttpServletRequest request,
			HttpServletResponse response) {

		userService.updateUserPassword(passwordChangeDTO.currentPassword(), userId, passwordChangeDTO.newPassword());

		Response responseObj = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.build();

		SecurityCookieUtils.eatAllCookies(request, response);

		return ResponseEntity.ok(responseObj);
	}

	@DeleteMapping()
	public ResponseEntity<Response> deleteUser(
			@RequestParam Long id,
			@RequestParam String password,
			HttpServletRequest request,
			HttpServletResponse response) {

		userService.deleteUserById(password, id);

		Response responseObj = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.build();

		SecurityCookieUtils.eatAllCookies(request, response);

		return ResponseEntity.ok(responseObj);
	}
}
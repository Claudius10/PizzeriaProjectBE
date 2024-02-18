package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.aop.annotations.ValidateUserId;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.user.dto.*;
import PizzaApp.api.repos.user.projections.UserProjection;
import PizzaApp.api.services.user.UserService;
import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<UserProjection> findUserById(@PathVariable Long userId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findDTOById(userId));
	}

	@ValidateUserId
	@GetMapping("/{userId}/address")
	public ResponseEntity<Set<Address>> findUserAddressListById(@PathVariable Long userId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAddressListById(userId));
	}

	@ValidateUserId
	@PostMapping("/{userId}/address")
	public ResponseEntity<?> createUserAddress(@PathVariable Long userId, @RequestBody @Valid Address address, HttpServletRequest request) {
		boolean accepted = userService.addAddress(userId, address);
		if (accepted) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg("Solo se permiten 3 domicilios almacenados.")
						.build());
	}

	@ValidateUserId
	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<?> deleteUserAddress(@PathVariable Long userId, @PathVariable Long addressId, HttpServletRequest request) {
		userService.removeAddress(userId, addressId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{userId}/name")
	public ResponseEntity<?> updateName(@PathVariable Long userId, @Valid @RequestBody NameChangeDTO nameChangeDTO) {
		userService.updateName(nameChangeDTO.password(), userId, nameChangeDTO.name());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/{userId}/email")
	public ResponseEntity<?> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailChangeDTO emailChangeDTO,
										 HttpServletRequest request, HttpServletResponse response) {
		userService.updateEmail(emailChangeDTO.password(), userId, emailChangeDTO.email());
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
		userService.updatePassword(passwordChangeDTO.currentPassword(), userId, passwordChangeDTO.newPassword());
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser
			(@PathVariable Long userId,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {
		userService.delete(passwordDTO.password(), userId);
		SecurityCookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

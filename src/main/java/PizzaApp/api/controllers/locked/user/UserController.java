package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.services.user.account.UserDataService;
import PizzaApp.api.services.user.account.UserService;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
import PizzaApp.api.utility.auth.CookieUtils;
import PizzaApp.api.validation.account.UserRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

	private final UserService userService;

	private final UserDataService userDataService;

	private final TelephoneService telephoneService;

	private final AddressService addressService;

	private final UserRequestValidator userRequestValidator;

	public UserController
			(UserService userService,
			 UserDataService userDataService,
			 TelephoneService telephoneService,
			 AddressService addressService,
			 UserRequestValidator userRequestValidator) {
		this.userService = userService;
		this.userDataService = userDataService;
		this.telephoneService = telephoneService;
		this.addressService = addressService;
		this.userRequestValidator = userRequestValidator;
	}

	// NOTE - endpoints for UserService

	@PutMapping("/{id}/name")
	public ResponseEntity<?> updateName
			(@PathVariable String id,
			 @Valid @RequestBody NameChangeDTO nameChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		userService.updateName(id, nameChangeDTO);

		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{id}/email")
	public ResponseEntity<?> updateEmail
			(@PathVariable String id,
			 @Valid @RequestBody EmailChangeDTO emailChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		userService.updateEmail(id, emailChangeDTO);

		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<?> updatePassword
			(@PathVariable String id,
			 @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {

		userRequestValidator.validate(id, request);
		userService.updatePassword(id, passwordChangeDTO);

		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete
			(@PathVariable String id,
			 @Valid @RequestBody PasswordDTO passwordDTO,
			 HttpServletRequest request,
			 HttpServletResponse response) {

		userRequestValidator.validate(id, request);
		userService.delete(id, passwordDTO);

		CookieUtils.eatAllCookies(request, response);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	// NOTE - endpoints for UserDataService

	@GetMapping("/{id}/data")
	public ResponseEntity<UserDataDTO> findUserDataDTOById
			(@PathVariable String id,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(userDataService.findDTOById(id));
	}

	@GetMapping("/{id}/tel")
	public ResponseEntity<List<TelephoneDTO>> findUserTelListById
			(@PathVariable String id,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(telephoneService.findAllByUserId(id));
	}

	@PostMapping("/{id}/tel")
	public ResponseEntity<?> addTel
			(@PathVariable String id,
			 @RequestBody @Valid Integer telephone,
			 HttpServletRequest request) {

		userRequestValidator.validate(id, request);
		userDataService.addTel(id, telephone);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{id}/tel")
	public ResponseEntity<?> removeTel
			(@PathVariable String id,
			 @RequestBody @Valid Integer telephone,
			 HttpServletRequest request) {

		userRequestValidator.validate(id, request);
		userDataService.removeTel(id, telephone);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("/{id}/address")
	public ResponseEntity<List<Address>> findUserAddressListById
			(@PathVariable String id,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(addressService.findAllByUserId(id));
	}

	@PostMapping("/{id}/address")
	public ResponseEntity<?> addAddress
			(@PathVariable String id,
			 @RequestBody @Valid Address address,
			 HttpServletRequest request) {

		userRequestValidator.validate(id, request);
		userDataService.addAddress(id, address);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{id}/address")
	public ResponseEntity<?> removeAddress
			(@PathVariable String id,
			 @RequestBody @Valid Address address,
			 HttpServletRequest request) {

		userRequestValidator.validate(id, request);
		userDataService.removeAddress(id, address);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}

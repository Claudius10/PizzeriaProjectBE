package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.services.user.account.UserDataService;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
import PizzaApp.api.validation.account.UserRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserDataController {

	private final UserDataService userDataService;

	private final TelephoneService telephoneService;

	private final AddressService addressService;

	private final UserRequestValidator userRequestValidator;

	public UserDataController(UserDataService userDataService,
							  TelephoneService telephoneService,
							  AddressService addressService,
							  UserRequestValidator userRequestValidator) {
		this.userDataService = userDataService;
		this.telephoneService = telephoneService;
		this.addressService = addressService;
		this.userRequestValidator = userRequestValidator;
	}

	@GetMapping("/{userId}/data/tel")
	public ResponseEntity<List<TelephoneDTO>> findUserTelListById
			(@PathVariable Long userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.OK).body(telephoneService.findAllByUserId(userId));
	}

	@PostMapping("/{userId}/data/tel")
	public ResponseEntity<?> createUserTel
			(@PathVariable Long userId,
			 @RequestBody @Valid Integer telephone,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userDataService.addTel(userId, telephone);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{userId}/data/tel/{telId}")
	public ResponseEntity<?> deleteUserTel
			(@PathVariable Long userId,
			 @PathVariable Long telId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userDataService.removeTel(userId, telId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("/{userId}/data/address")
	public ResponseEntity<List<Address>> findUserAddressListById
			(@PathVariable Long userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.OK).body(addressService.findAllByUserId(userId));
	}

	@PostMapping("/{userId}/data/address")
	public ResponseEntity<?> createUserAddress
			(@PathVariable Long userId,
			 @RequestBody @Valid Address address,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userDataService.addAddress(userId, address);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{userId}/data/address/{addressId}")
	public ResponseEntity<?> deleteUserAddress
			(@PathVariable Long userId,
			 @PathVariable Long addressId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		userDataService.removeAddress(userId, addressId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}

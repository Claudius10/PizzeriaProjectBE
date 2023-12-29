package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.services.user.account.UserDataService;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
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

	public UserDataController
			(UserDataService userDataService,
			 TelephoneService telephoneService,
			 AddressService addressService) {
		this.userDataService = userDataService;
		this.telephoneService = telephoneService;
		this.addressService = addressService;
	}

	@GetMapping("/{userId}/tel")
	public ResponseEntity<List<TelephoneDTO>> findUserTelListById(@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(telephoneService.findAllByUserId(userId));
	}

	@PostMapping("/{userId}/tel")
	public ResponseEntity<?> createUserTel(@PathVariable Long userId, @RequestBody @Valid Integer telephone) {
		String isValid = userDataService.addTel(userId, telephone);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isValid);
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
	}

	@DeleteMapping("/{userId}/tel/{telId}")
	public ResponseEntity<?> deleteUserTel(@PathVariable Long userId, @PathVariable Long telId) {
		userDataService.removeTel(userId, telId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("/{userId}/address")
	public ResponseEntity<List<Address>> findUserAddressListById(@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(addressService.findAllByUserId(userId));
	}

	@PostMapping("/{userId}/address")
	public ResponseEntity<?> createUserAddress(@PathVariable Long userId, @RequestBody @Valid Address address) {
		String isValid = userDataService.addAddress(userId, address);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isValid);
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
	}

	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<?> deleteUserAddress(@PathVariable Long userId, @PathVariable Long addressId) {
		userDataService.removeAddress(userId, addressId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}

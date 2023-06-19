package PizzaApp.api.controllers.locked.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.user.common.Address;
import PizzaApp.api.services.address.AddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping("/address")
	public ResponseEntity<Address> findAddress(@RequestBody @Valid Address address) {
		return new ResponseEntity<>(addressService.findAddress(address), HttpStatus.OK);
	}
}

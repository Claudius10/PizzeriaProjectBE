package PizzaApp.api.address;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.services.address.AddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TestAddressController {

	private final AddressService addressService;

	public TestAddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping("/address")
	public ResponseEntity<Address> findAddress(@RequestBody @Valid Address address) {
		return ResponseEntity.status(HttpStatus.OK).body(addressService.find(address).get());
	}
}

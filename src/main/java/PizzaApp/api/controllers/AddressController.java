package PizzaApp.api.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.services.address.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {
	
	private AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}
	
	// endpoint for testing purposes
	@GetMapping()
	public ResponseEntity<List<Address>> findAddresses(@RequestBody Address address) {
		return new ResponseEntity<List<Address>>(addressService.findAddresses(address), HttpStatus.OK);
	}
}

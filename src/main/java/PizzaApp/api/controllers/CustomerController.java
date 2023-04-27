package PizzaApp.api.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.services.customer.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	// endpoint for testing purposes
	@GetMapping()
	public ResponseEntity<List<Customer>> findCustomers(@RequestBody Customer customer) {
		return new ResponseEntity<List<Customer>>(customerService.findCustomers(customer), HttpStatus.OK);
	}
}

package PizzaApp.api.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.services.telephone.TelephoneService;

@RestController
@RequestMapping("/api/telephone")
public class TelephoneController {

	private TelephoneService telephoneService;

	public TelephoneController(TelephoneService telephoneService) {
		this.telephoneService = telephoneService;
	}

	// endpoint for testing purposes
	@GetMapping("/test/{number}")
	public ResponseEntity<List<Telephone>> findByNumber(@PathVariable int number) {

		Telephone tel = new Telephone();
		tel.setNumber(number);

		return new ResponseEntity<List<Telephone>>(telephoneService.findAllByNumber(tel), HttpStatus.OK);
	}
}
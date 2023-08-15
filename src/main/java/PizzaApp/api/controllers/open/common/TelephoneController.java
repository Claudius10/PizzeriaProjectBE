package PizzaApp.api.controllers.open.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.user.Telephone;
import PizzaApp.api.services.user.telephone.TelephoneService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TelephoneController {

	private final TelephoneService telephoneService;

	public TelephoneController(TelephoneService telephoneService) {
		this.telephoneService = telephoneService;
	}

/*
	@GetMapping("/telephone")
	public ResponseEntity<Telephone> findByNumber(@RequestBody @Valid Telephone tel) {
		return new ResponseEntity<>(telephoneService.findByNumber(tel), HttpStatus.OK);
	}*/
}
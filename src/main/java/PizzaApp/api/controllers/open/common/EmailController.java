package PizzaApp.api.controllers.open.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.common.Email;
import PizzaApp.api.services.common.email.EmailService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EmailController {

	private final EmailService emailService;

	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@GetMapping("/email")
	public ResponseEntity<Email> findByAddress(@RequestBody @Valid Email email) {
		return new ResponseEntity<>(emailService.findByAddress(email), HttpStatus.OK);
	}
}

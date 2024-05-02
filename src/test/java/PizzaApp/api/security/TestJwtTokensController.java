package PizzaApp.api.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
public class TestJwtTokensController {

	public TestJwtTokensController() {
	}

	@GetMapping()
	public ResponseEntity<?> testGetEndpoint() {
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping()
	public ResponseEntity<?> testPostEndpoint() {
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}

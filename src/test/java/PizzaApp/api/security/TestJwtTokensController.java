package PizzaApp.api.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

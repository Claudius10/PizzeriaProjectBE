package PizzaApp.api.controllers.open;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.order.dto.CreatedAnonOrderDTO;
import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.services.user.UserService;
import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anon")
@Validated
public class AnonController {

	private final UserService userService;

	private final OrderService orderService;

	public AnonController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(registerDTO));
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.BAD_REQUEST.value())
							.withPath(request.getServletPath())
							.withErrorMsg(ValidationResponses.EMAIL_ALREADY_EXISTS)
							.build());
		}
	}

	@PostMapping("/order")
	public ResponseEntity<CreatedAnonOrderDTO> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrder, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.createAnonOrder(newAnonOrder));
	}
}
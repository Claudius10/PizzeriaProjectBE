package PizzaApp.api.controllers.open.common;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.services.user.account.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.order.OrderService;
import jakarta.validation.Valid;

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
	public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO) {
		try {
			userService.create(registerDTO);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Una cuenta ya existe con el correo electrónico introducido. Si no recuerda la " +
					"contraseña, contacte con nosotros");
		}
	}

	@PostMapping("/order")
	public ResponseEntity<String> createAnonOrder(@RequestBody @Valid Order order) {
		String isValid = orderService.createAnonOrder(order);
		if (isValid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isValid);
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
	}
}
package PizzaApp.api.controllers.open.common;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.services.user.account.UserService;
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
		userService.create(registerDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/order")
	public ResponseEntity<Long> createAnonOrder(@RequestBody @Valid Order order) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createAnonOrder(order));
	}
}
package PizzaApp.api.controllers.open.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.order.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
@Validated
public class AnonOrderController {

	private final OrderService orderService;

	public AnonOrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping()
	public ResponseEntity<Long> createAnonOrder(@RequestBody @Valid Order order) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createAnonOrder(order));
	}
}
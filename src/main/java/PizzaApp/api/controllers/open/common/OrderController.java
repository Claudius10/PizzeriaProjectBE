package PizzaApp.api.controllers.open.common;

import java.time.LocalDateTime;

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
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping()
	public ResponseEntity<Long> createOrder(@RequestBody @Valid Order order) {
		// set created on
		order.setCreatedOn(LocalDateTime.now());
		Long id = orderService.createAnonOrder(order);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}
}
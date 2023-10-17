package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.NewUserOrderDTO;
import PizzaApp.api.entity.dto.user.UpdateUserOrderDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.validation.account.UserRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/orders")
@Validated
public class UserOrdersController {

	private final OrderService orderService;

	private final UserRequestValidator userRequestValidator;

	public UserOrdersController
			(OrderService orderService,
			 UserRequestValidator userRequestValidator) {
		this.orderService = orderService;
		this.userRequestValidator = userRequestValidator;
	}

	@PostMapping("/{userId}")
	public ResponseEntity<Long> createUserOrder
			(@PathVariable Long userId,
			 @RequestBody NewUserOrderDTO order,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createUserOrder(order));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> findUserOrder(@PathVariable Long orderId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrder(orderId));
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<OrderPaginationResultDTO> findUserOrders
			(@PathVariable Long userId,
			 @RequestParam Integer pageSize,
			 @RequestParam Integer pageNumber,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrdersSummary(userId, pageSize, pageNumber));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Long> updateUserOrder
			(@RequestBody UpdateUserOrderDTO order,
			 @PathVariable Long userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderService.updateUserOrder(order));
	}

	@DeleteMapping("/{userId}/{orderId}")
	public ResponseEntity<Long> deleteUserOrderById(
			@PathVariable Long orderId,
			@PathVariable Long userId,
			HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		orderService.deleteUserOrderById(orderId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
	}
}

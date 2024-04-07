package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.aop.annotations.ValidateUserId;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.repos.order.projections.OrderSummary;
import PizzaApp.api.services.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/orders")
@Validated
public class UserOrdersController {

	private final OrderService orderService;

	public UserOrdersController(OrderService orderService) {
		this.orderService = orderService;
	}

	@ValidateUserId
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> findUserOrderDTO(@PathVariable Long orderId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findDTOById(orderId));
	}

	@ValidateUserId
	@PostMapping()
	public ResponseEntity<Long> createUserOrder(@RequestBody NewUserOrderDTO order, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.createUserOrder(order));
	}

	@ValidateUserId
	@PutMapping()
	public ResponseEntity<Long> updateUserOrder(@RequestBody UpdateUserOrderDTO order, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.updateUserOrder(order));
	}

	@ValidateUserId
	@DeleteMapping(path = "/{orderId}")
	public ResponseEntity<Long> deleteUserOrderById(@PathVariable Long orderId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteUserOrderById(orderId));
	}

	@ValidateUserId
	@GetMapping(params = {"pageNumber", "pageSize", "userId"})
	public ResponseEntity<Page<OrderSummary>> findUserOrdersSummary
			(@RequestParam Long userId,
			 @RequestParam Integer pageSize,
			 @RequestParam Integer pageNumber,
			 HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrderSummary(userId, pageSize, pageNumber));
	}
}
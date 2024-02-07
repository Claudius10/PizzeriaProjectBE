package PizzaApp.api.controllers.locked;

import PizzaApp.api.entity.dto.order.OrderDTOPojo;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.order.UserOrderDTO;
import PizzaApp.api.entity.dto.order.UpdateUserOrderDTO;
import PizzaApp.api.services.order.OrderService;
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

	public UserOrdersController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping()
	public ResponseEntity<String> createUserOrder(@RequestBody UserOrderDTO order, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.createUserOrder(order));
	}

	@PutMapping()
	public ResponseEntity<String> updateUserOrder(@RequestBody UpdateUserOrderDTO order, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.updateUserOrder(order));
	}

	@DeleteMapping(path = "/{orderId}", params = "userId")
	public ResponseEntity<String> deleteUserOrderById(@PathVariable Long orderId, @RequestParam Long userId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteUserOrderById(orderId, userId));
	}

	@GetMapping(params = {"pageNumber", "pageSize", "userId"})
	public ResponseEntity<OrderPaginationResultDTO> findUserOrdersSummary
			(@RequestParam Long userId,
			 @RequestParam Integer pageSize,
			 @RequestParam Integer pageNumber) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrdersSummary(userId, pageSize, pageNumber));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTOPojo> findUserOrderDTO(@PathVariable Long orderId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrderDTO(orderId));
	}
}

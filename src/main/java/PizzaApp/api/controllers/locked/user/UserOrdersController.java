package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTOPojo;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.order.UserOrderDTO;
import PizzaApp.api.entity.dto.order.UpdateUserOrderDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utility.string.StringUtils;
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
	public ResponseEntity<String> createUserOrder(@RequestBody UserOrderDTO order) {
		String result = orderService.createUserOrder(order);
		if (StringUtils.isNumber(result)) {
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
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

	@PutMapping()
	public ResponseEntity<String> updateUserOrder(@RequestBody UpdateUserOrderDTO order) {
		String result = orderService.updateUserOrder(order);
		if (StringUtils.isNumber(result)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteUserOrderById(@PathVariable Long orderId) {
		String result = orderService.deleteUserOrderById(orderId);
		if (StringUtils.isNumber(result)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
}

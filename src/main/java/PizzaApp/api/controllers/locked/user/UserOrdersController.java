package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTOPojo;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.order.UserOrderDTO;
import PizzaApp.api.entity.dto.order.UpdateUserOrderDTO;
import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utility.string.StringUtils;
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
	public ResponseEntity<?> createUserOrder(@RequestBody UserOrderDTO order, HttpServletRequest request) {
		String result = orderService.createUserOrder(order);
		return getResponseEntity(request, result);
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
	public ResponseEntity<?> updateUserOrder(@RequestBody UpdateUserOrderDTO order, HttpServletRequest request) {
		String result = orderService.updateUserOrder(order);
		return getResponseEntity(request, result);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<?> deleteUserOrderById(@PathVariable Long orderId, HttpServletRequest request) {
		String result = orderService.deleteUserOrderById(orderId);
		return getResponseEntity(request, result);
	}

	private ResponseEntity<?> getResponseEntity(HttpServletRequest request, String result) {
		if (StringUtils.isNumber(result)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.BAD_REQUEST.value())
							.withPath(request.getServletPath())
							.withErrorMsg(result)
							.build());
		}
	}
}

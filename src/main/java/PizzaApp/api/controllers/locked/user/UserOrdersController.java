package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.NewUserOrderDTO;
import PizzaApp.api.entity.dto.user.UpdateUserOrderDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utility.auth.CookieUtils;
import PizzaApp.api.validation.account.UserRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
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
			(@PathVariable String userId,
			 @RequestBody NewUserOrderDTO order,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(userId, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createUserOrder(order));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> findUserOrder
			(@PathVariable String orderId,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrder(orderId));
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<OrderPaginationResultDTO> findOrdersSummary
			(@PathVariable String userId,
			 @RequestParam String pageSize,
			 @RequestParam String pageNumber,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(userId, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrdersSummary(userId, pageSize, pageNumber));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Long> updateUserOrder
			(@RequestBody UpdateUserOrderDTO order,
			 @PathVariable String userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderService.updateUserOrder(order));
	}

	// TODO - verify user id also matches id in jwt claim before deleting
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteById(@PathVariable String orderId) {
		orderService.deleteById(orderId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
	}
}

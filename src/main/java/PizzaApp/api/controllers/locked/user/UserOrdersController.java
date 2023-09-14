package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
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

	@PostMapping("/{id}")
	public ResponseEntity<Long> createUserOrder
			(@PathVariable String id,
			 @RequestBody UserOrderDTO order,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		userRequestValidator.validate(id, request);
		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createUserOrder(order));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> findUserOrder
			(@PathVariable String id,
			 HttpServletResponse response,
			 CsrfToken csrfToken) {

		CookieUtils.loadCsrf(response, csrfToken);
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findUserOrder(id));
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
			(@RequestBody UserOrderDTO order,
			 @PathVariable String userId,
			 HttpServletRequest request) {

		userRequestValidator.validate(userId, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderService.updateUserOrder(order));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable String id) {
		orderService.deleteById(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(id);
	}
}

package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
import PizzaApp.api.validation.account.AccountRequestValidator;
import PizzaApp.api.services.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/orders")
@Validated
public class AccountOrders {

	private final OrderService orderService;

	private final AccountRequestValidator accountRequestValidator;

	public AccountOrders(OrderService orderService, AccountRequestValidator accountRequestValidator) {
		this.orderService = orderService;
		this.accountRequestValidator = accountRequestValidator;
	}

	@PostMapping("/{id}")
	public ResponseEntity<Long> createUserOrder(
			@PathVariable String id,
			@RequestBody UserOrderDTO order,
			HttpServletRequest request) {
		accountRequestValidator.validate(id, request);
		Long orderId = orderService.createUserOrder(order);
		return new ResponseEntity<>(orderId, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> findUserOrder(
			@PathVariable String id) {
		return new ResponseEntity<>(orderService.findUserOrder(id), HttpStatus.OK);
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<OrderPaginationResultDTO> findOrdersSummary(
			@PathVariable String userId,
			@RequestParam String pageSize,
			@RequestParam String pageNumber,
			HttpServletRequest request) {
		accountRequestValidator.validate(userId, request);
		return ResponseEntity.ok().body(orderService.findOrdersSummary(userId, pageSize, pageNumber));

	}

	@PutMapping("/{userId}")
	public ResponseEntity<Long> updateUserOrder(
			@RequestBody @Valid UserOrderDTO order,
			@PathVariable String userId,
			HttpServletRequest request) {
		accountRequestValidator.validate(userId, request);
		Long id = orderService.updateUserOrder(order);
		return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
	}

	// path variable validation not needed
	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteById(@PathVariable Long id) {
		orderService.deleteById(id);
		return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
	}
}

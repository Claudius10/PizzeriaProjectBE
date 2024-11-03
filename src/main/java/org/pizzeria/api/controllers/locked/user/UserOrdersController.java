package org.pizzeria.api.controllers.locked.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.pizzeria.api.aop.annotations.ValidateUserId;
import org.pizzeria.api.entity.order.dto.NewUserOrderDTO;
import org.pizzeria.api.entity.order.dto.OrderDTO;
import org.pizzeria.api.entity.order.dto.OrderSummaryListDTO;
import org.pizzeria.api.entity.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.repos.order.projections.OrderSummary;
import org.pizzeria.api.services.order.OrderService;
import org.pizzeria.api.utils.globals.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/{userId}/order")
@Validated
public class UserOrdersController {

	private final OrderService orderService;

	public UserOrdersController(OrderService orderService) {
		this.orderService = orderService;
	}

	@ValidateUserId
	@PostMapping
	public ResponseEntity<Long> createUserOrder(@RequestBody @Valid NewUserOrderDTO order, @PathVariable Long userId, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createUserOrder(userId, order));
	}

	@ValidateUserId
	@GetMapping("/{orderId}")
	public ResponseEntity<Object> findUserOrderDTO(@PathVariable Long orderId, @PathVariable Long userId, HttpServletRequest request) {
		Optional<OrderDTO> order = orderService.findDTOById(orderId);
		return order.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.accepted().body(String.format(ApiResponses.ORDER_NOT_FOUND, orderId)));
	}

	@ValidateUserId
	@PutMapping("/{orderId}")
	public ResponseEntity<String> updateUserOrder(@PathVariable Long orderId, @RequestBody @Valid UpdateUserOrderDTO order, @PathVariable Long userId, HttpServletRequest request) {
		Long result = orderService.updateUserOrder(orderId, order);

		if (result == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
					ApiResponses.UPDATE_USER_ORDER_ERROR, orderId, order.getAddressId()));
		}

		return ResponseEntity.ok().body(String.valueOf(result));
	}

	@ValidateUserId
	@DeleteMapping(path = "/{orderId}")
	public ResponseEntity<String> deleteUserOrderById(@PathVariable Long orderId, @PathVariable Long userId, HttpServletRequest request) {
		Long id = orderService.deleteUserOrderById(orderId);
		// NOTE - there's no need to check whatever id is null here
		//  because if it is, OrderValidatorImpl will catch it
		return ResponseEntity.ok(String.valueOf(id));
	}

	@ValidateUserId
	@GetMapping(path = "/all")
	public ResponseEntity<Object> findUserOrdersSummary(
			@RequestParam(name = "pageNumber") Integer pageNumber,
			@RequestParam(name = "pageSize") Integer pageSize,
			@PathVariable Long userId,
			HttpServletRequest request) {

		Page<OrderSummary> orderSummaryPage = orderService.findUserOrderSummary(userId, pageSize, pageNumber);
		List<OrderSummary> content = orderSummaryPage.getContent();
		int totalPages = orderSummaryPage.getTotalPages();

		OrderSummaryListDTO orders = new OrderSummaryListDTO(content, totalPages);
		return ResponseEntity.ok(orders);
	}
}
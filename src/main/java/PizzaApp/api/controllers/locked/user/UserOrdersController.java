package PizzaApp.api.controllers.locked.user;

import PizzaApp.api.aop.annotations.ValidateUserId;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.repos.order.projections.OrderSummary;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
		Optional<OrderDTO> order = orderService.findDTOById(orderId);
		return order
				.map(orderDTO -> ResponseEntity.ok().body(orderDTO))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@ValidateUserId
	@PostMapping
	public ResponseEntity<Long> createUserOrder(@RequestBody @Valid NewUserOrderDTO order, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createUserOrder(order));
	}

	@ValidateUserId
	@PutMapping
	public ResponseEntity<String> updateUserOrder(@RequestBody @Valid UpdateUserOrderDTO order, HttpServletRequest request) {
		Long result = orderService.updateUserOrder(order);

		if (result == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
					ValidationResponses.UPDATE_USER_ORDER_ERROR, order.getOrderId(), order.getAddressId()));
		}

		return ResponseEntity.ok().body(String.valueOf(result));
	}

	@ValidateUserId
	@DeleteMapping(path = "/{orderId}")
	public ResponseEntity<String> deleteUserOrderById(@PathVariable Long orderId, HttpServletRequest request) {
		return ResponseEntity.ok().body(String.valueOf(orderService.deleteUserOrderById(orderId)));
	}

	@ValidateUserId
	@GetMapping(params = {"pageNumber", "pageSize", "userId"})
	public ResponseEntity<Page<OrderSummary>> findUserOrdersSummary(@RequestParam Long userId, @RequestParam Integer pageSize,
																	@RequestParam Integer pageNumber, HttpServletRequest request) {
		Page<OrderSummary> orderSummaryPage = orderService.findUserOrderSummary(userId, pageSize, pageNumber);

		if (!orderSummaryPage.hasContent()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(orderSummaryPage);
	}
}
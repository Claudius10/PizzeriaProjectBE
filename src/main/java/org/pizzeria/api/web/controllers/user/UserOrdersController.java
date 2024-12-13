package org.pizzeria.api.web.controllers.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.pizzeria.api.services.order.OrderService;
import org.pizzeria.api.web.aop.annotations.ValidateUserId;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.order.dto.NewUserOrderDTO;
import org.pizzeria.api.web.dto.order.dto.OrderDTO;
import org.pizzeria.api.web.dto.order.dto.OrderSummaryListDTO;
import org.pizzeria.api.web.dto.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.web.dto.order.projection.OrderSummaryProjection;
import org.pizzeria.api.web.globals.ApiResponses;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.USER_ID + ApiRoutes.ORDER_BASE)
@Validated
public class UserOrdersController {

	private final OrderService orderService;

	public UserOrdersController(OrderService orderService) {
		this.orderService = orderService;
	}

	@ValidateUserId
	@PostMapping
	public ResponseEntity<Response> createUserOrder(@RequestBody @Valid NewUserOrderDTO order, @PathVariable Long userId, HttpServletRequest request) {

		Long id = orderService.createUserOrder(userId, order);

		Response response = Response.builder()
				.statusDescription(HttpStatus.CREATED.name())
				.statusCode(HttpStatus.CREATED.value())
				.data(id)
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@ValidateUserId
	@GetMapping(ApiRoutes.ORDER_ID)
	public ResponseEntity<Response> findUserOrderDTO(@PathVariable Long orderId, @PathVariable Long userId, HttpServletRequest request) {

		Optional<OrderDTO> projectionById = orderService.findProjectionById(orderId);

		Response response = Response.builder()
				.statusDescription(projectionById.isPresent() ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(projectionById.isPresent() ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(projectionById.isPresent() ? null : ApiResponses.ORDER_NOT_FOUND)
				.errorMessage(String.valueOf(orderId))
				.data(projectionById.orElse(null))
				.build();

		return ResponseEntity.status(projectionById.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(response);
	}

	@ValidateUserId
	@PutMapping(ApiRoutes.ORDER_ID)
	public ResponseEntity<Response> updateUserOrder(
			@PathVariable Long orderId,
			@RequestBody @Valid UpdateUserOrderDTO order,
			@PathVariable Long userId,
			HttpServletRequest request) {

		boolean result = orderService.updateUserOrder(orderId, order);

		Response response = Response.builder()
				.statusDescription(result ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(result ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(result ? null : ApiResponses.ORDER_NOT_FOUND)
				.data(orderId)
				.build();

		return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(response);
	}

	@ValidateUserId
	@DeleteMapping(ApiRoutes.ORDER_ID)
	public ResponseEntity<Response> deleteUserOrderById(@PathVariable Long orderId, @PathVariable Long userId, HttpServletRequest request) {
		// NOTE - there's no need to check whatever id is null here because if it is, OrderValidatorImpl will catch it

		boolean result = orderService.deleteUserOrderById(orderId);

		Response response = Response.builder()
				.statusDescription(result ? HttpStatus.OK.name() : HttpStatus.NO_CONTENT.name())
				.statusCode(result ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value())
				.errorClass(result ? null : ApiResponses.ORDER_NOT_FOUND)
				.data(orderId)
				.build();

		return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(response);
	}

	@ValidateUserId
	@GetMapping(ApiRoutes.ORDER_SUMMARY)
	public ResponseEntity<Response> findUserOrdersSummary(
			@RequestParam(name = ApiRoutes.ORDER_SUMMARY_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = ApiRoutes.ORDER_SUMMARY_PAGE_SIZE) Integer pageSize,
			@PathVariable Long userId,
			HttpServletRequest request) {

		Page<OrderSummaryProjection> orderSummaryPage = orderService.findUserOrderSummary(userId, pageSize, pageNumber);

		OrderSummaryListDTO orders = new OrderSummaryListDTO(
				orderSummaryPage.getContent(),
				orderSummaryPage.getTotalPages(),
				orderSummaryPage.getPageable().getPageSize(),
				orderSummaryPage.getTotalElements(),
				orderSummaryPage.hasNext()
		);

		Response response = Response.builder()
				.statusDescription(HttpStatus.OK.name())
				.statusCode(HttpStatus.OK.value())
				.data(orders)
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
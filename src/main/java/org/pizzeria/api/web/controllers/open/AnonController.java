package org.pizzeria.api.web.controllers.open;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.pizzeria.api.services.order.OrderService;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.web.dto.order.dto.CreatedAnonOrderDTO;
import org.pizzeria.api.web.dto.order.dto.NewAnonOrderDTO;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE)
@Validated
public class AnonController {

	private final UserService userService;

	private final OrderService orderService;

	public AnonController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@PostMapping(ApiRoutes.ANON_REGISTER)
	public ResponseEntity<Response> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {

		userService.createUser(registerDTO);

		Response response = Response.builder()
				.statusDescription(HttpStatus.CREATED.toString())
				.statusCode(HttpStatus.CREATED.value())
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping(ApiRoutes.ANON_ORDER)
	public ResponseEntity<Response> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrder, HttpServletRequest request) {
		CreatedAnonOrderDTO createdOrder = orderService.createAnonOrder(newAnonOrder);

		Response response = Response.builder()
				.statusDescription(HttpStatus.CREATED.toString())
				.statusCode(HttpStatus.CREATED.value())
				.payload(createdOrder)
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
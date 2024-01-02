package PizzaApp.api.controllers.open.common;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
import PizzaApp.api.services.user.account.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.order.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/anon")
@Validated
public class AnonController {

	private final UserService userService;
	private final OrderService orderService;

	public AnonController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
		try {
			userService.create(registerDTO);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.BAD_REQUEST.value())
							.withPath(request.getServletPath())
							.withErrorMsg("Una cuenta ya existe con el correo electrónico introducido. Si no recuerda la " +
									"contraseña, contacte con nosotros")
							.build());
		}
	}

	@PostMapping("/order")
	public ResponseEntity<?> createAnonOrder(@RequestBody @Valid Order order, HttpServletRequest request) {
		String isInvalid = orderService.createAnonOrder(order);
		if (isInvalid != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.BAD_REQUEST.value())
							.withPath(request.getServletPath())
							.withErrorMsg(isInvalid)
							.build());
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
	}
}
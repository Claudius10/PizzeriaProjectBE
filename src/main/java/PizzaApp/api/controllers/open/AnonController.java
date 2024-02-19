package PizzaApp.api.controllers.open;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
			return ResponseEntity.status(HttpStatus.OK).body(userService.create(registerDTO));
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiErrorDTO.Builder()
							.withStatusCode(HttpStatus.BAD_REQUEST.value())
							.withPath(request.getServletPath())
							.withErrorMsg("Una cuenta ya existe con el correo electrónico introducido. Si no recuerda la contraseña, contacte con nosotros")
							.build());
		}
	}

	@PostMapping("/order")
	public ResponseEntity<Long> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrder, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.createAnonOrder(newAnonOrder));
	}
}
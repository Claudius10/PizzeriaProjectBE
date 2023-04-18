package PizzaApp.api.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.order.OrdersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://192.168.1.11:3000")
@Validated
public class OrdersRestController {

	private OrdersService ordersService;

	public OrdersRestController(OrdersService ordersService) {
		this.ordersService = ordersService;
	}

	@PostMapping("/order")
	public ResponseEntity<Long> createOrder(@Valid @RequestBody Order order) {
		order.setId((long) 0);
		ordersService.createOrUpdate(order);
		return new ResponseEntity<Long>(order.getId(), HttpStatus.CREATED);
	}

	@PutMapping("/order")
	public ResponseEntity<Long> updateOrder(@Valid @RequestBody Order order) {
		ordersService.createOrUpdate(order);
		// returning below ResponseEntity with statusCode is
		// for front-end SaveOrUpdateOrder query fn
		// in order to correctly either return responseBody or throw if error
		return new ResponseEntity<Long>(order.getId(), HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/{id}")
	public ResponseEntity<Order> findOrderById(
			@PathVariable @Pattern(regexp = "^[0-9]{1,10}$", message = "El valor tiene que ser un número positivo. Máximo 10 digitos") String id) {
		Long orderId = Long.parseLong(id);
		// same as others, returning 200's code makes front-end return the responseBody
		// !response.ok from fetch API throws the responseBody with handled exception from back-end
		return new ResponseEntity<Order>(ordersService.findById(orderId), HttpStatus.OK);
	}

	// delete mapping path variable doesn't need validation
	// since on the front end, the deleteOrderById query fn
	// uses the id from the fetched order w/ findOrderById
	// not from user input
	@DeleteMapping("/order/{id}")
	public ResponseEntity<Long> deleteById(@PathVariable Long id) {
		ordersService.deleteById(id);
		return new ResponseEntity<Long>(id, HttpStatus.ACCEPTED);
	}
}
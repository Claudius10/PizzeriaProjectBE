package PizzaApp.api.controllers;

import java.time.LocalDateTime;

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

import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.services.order.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api")
// "https://pizzeria-project-claudius10.vercel.app"
@CrossOrigin(origins = "http://192.168.1.11:3000")
@Validated
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/order")
	public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
		// set created on
		order.setCreatedOn(LocalDateTime.now());

		Order theOrder = orderService.createOrUpdate(order);
		return new ResponseEntity<Order>(theOrder, HttpStatus.CREATED);
	}

	@PutMapping("/order")
	public ResponseEntity<Order> updateOrder(@RequestBody @Valid Order order) {
		// set updated on
		order.setUpdatedOn(LocalDateTime.now());

		Order theOrder = orderService.createOrUpdate(order);
		// returning below ResponseEntity with statusCode is
		// for front-end SaveOrUpdateOrder query fn
		// in order to correctly either return responseBody or throw if error
		// based on the statusCode
		return new ResponseEntity<>(theOrder, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/{id}")
	public ResponseEntity<OrderDTO> findOrderById(
			@PathVariable @Pattern(regexp = "^[0-9]{1,10}$", message = "El valor tiene que ser un número positivo. Máximo 10 digitos") String id) {
		Long orderId = Long.parseLong(id);
		// same as others, returning 200's code makes front-end return the responseBody
		// !response.ok from fetch API throws the responseBody with handled exception
		// from back-end
		return new ResponseEntity<OrderDTO>(orderService.findDTOById(orderId), HttpStatus.OK);
	}

	// delete mapping path variable doesn't need validation
	// since on the front end, the deleteOrderById query fn
	// uses the id from the fetched order w/ findOrderById
	// not from user input
	@DeleteMapping("/order/{id}")
	public ResponseEntity<Long> deleteById(@PathVariable Long id) {
		orderService.deleteById(id);
		return new ResponseEntity<Long>(id, HttpStatus.ACCEPTED);
	}
}
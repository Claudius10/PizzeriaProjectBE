package PizzaApp.api.controllers;
import java.util.List;
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
	public Long createOrder(@Valid @RequestBody Order order) {
		order.setId((long) 0);
		ordersService.createOrder(order);
		return order.getId();
	}

	@PutMapping("/order")
	public void updateOrder(@Valid @RequestBody Order order) {
		ordersService.createOrder(order);
	}

	@GetMapping("/order/{id}")
	public Order findOrderById(
			@PathVariable @Pattern(regexp = "^[0-9]+$", message = "El valor tiene que ser un número positivo.") String id) {
		Long orderId = Long.parseLong(id);
		return ordersService.findOrderById(orderId);
	}

	// delete mapping path variable doesn't need validation
	// since it sends the id from the fetched order not from
	// an user input
	@DeleteMapping("/order/{id}")
	public void deleteOrderById(@PathVariable Long id) {
		ordersService.deleteOrderById(id);
	}

	// other Order endpoints not currently in use

	@GetMapping("/orders")
	public List<Order> getOrders() {
		return ordersService.getOrders();
	}

	@GetMapping("/orders/{storeName}")
	public List<Order> getOrdersByStore(@PathVariable String storeName) {
		return ordersService.getOrdersByStore(storeName);
	}

	@GetMapping("/orders/customer/{customerId}")
	public List<Order> getOrdersByCustomer(@PathVariable Long customerId) {
		return ordersService.getOrdersByCustomer(customerId);
	}
}

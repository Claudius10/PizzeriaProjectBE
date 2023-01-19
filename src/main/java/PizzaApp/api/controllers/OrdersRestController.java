package PizzaApp.api.controllers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.Order;
import PizzaApp.api.services.OrdersService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersRestController {

	private OrdersService ordersService;

	public OrdersRestController(OrdersService ordersService) {
		this.ordersService = ordersService;
	}
	
	@PostMapping("/order")
	public Order createOrder(@RequestBody Order order ) {
		ordersService.createOrder(order);
		return order;
	}
}

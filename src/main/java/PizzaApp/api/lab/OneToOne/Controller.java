package PizzaApp.api.lab.OneToOne;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lab")
public class Controller {

	// controller normally works with service
	// for lab, just repo
	private GeneralService service;

	public Controller(GeneralService service) {
		this.service = service;
	}

	// get order
	@GetMapping("/order/{orderId}")
	public OrderLab findOrderById(@PathVariable Long orderId) {
		return service.findOrderById(orderId);
	}

	// get address uni
	@GetMapping("/address/{addressId}")
	public AddressLab findAddressById(@PathVariable Long addressId) {
		return service.findAddressById(addressId);
	}

	// get address bi
	@GetMapping("/BiAddress/{addressId}")
	public AddressLabBi findBiAddressById(@PathVariable Long addressId) {
		return service.findBiAddressById(addressId);
	}

	@GetMapping("/order")
	public List<OrderLab> findAllOrders() {
		return service.findAllOrders();
	}

	// create order
	@PostMapping("/order")
	public OrderLab createOrder(@RequestBody OrderLab order) {
		order.setId((long) 0);
		service.createOrUpdateOrder(order);
		return order;
	}

	// update order
	@PutMapping("/order")
	public OrderLab updateOrder(@RequestBody OrderLab order) {
		service.createOrUpdateOrder(order);
		return order;
	}

	// delete order
	@DeleteMapping("/{orderId}")
	public String deleteOrderById(@PathVariable Long orderId) {
		service.deleteOrderById(orderId);
		return "Order with Id " + orderId + " deleted.";
	}

}

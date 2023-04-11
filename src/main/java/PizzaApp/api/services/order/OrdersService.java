package PizzaApp.api.services.order;
import java.util.List;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.OrdersRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersService {

	private OrdersRepository ordersRepo;

	public OrdersService(OrdersRepository ordersRepo) {
		this.ordersRepo = ordersRepo;
	}

	public void createOrder(Order order) {
		ordersRepo.createOrder(order);
	}

	public List<Order> getOrders() {
		return ordersRepo.getOrders();
	}

	public Order findOrderById(Long id) {
		return ordersRepo.findOrderById(id);
	}

	public List<Order> getOrdersByStore(String storeName) {
		return ordersRepo.getOrdersByStore(storeName);
	}

	public List<Order> getOrdersByCustomer(Long customerId) {
		return ordersRepo.getOrdersByCustomer(customerId);
	}

	public void deleteOrderById(Long id) {
		ordersRepo.deleteOrderById(id);
	}
}

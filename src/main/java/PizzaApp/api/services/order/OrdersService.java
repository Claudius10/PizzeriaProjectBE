package PizzaApp.api.services.order;
import java.util.List;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.OrdersRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersService {

	private OrdersRepository orderRepository;

	public OrdersService(OrdersRepository ordersRepo) {
		this.orderRepository = ordersRepo;
	}

	public void createOrUpdate(Order order) {
		orderRepository.createOrUpdate(order);
	}
	
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}

	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public List<Order> findAllByStore(String storeName) {
		return orderRepository.findAllByStore(storeName);
	}

	public List<Order> findAllByCustomer(Long customerId) {
		return orderRepository.findAllByCustomer(customerId);
	}
}

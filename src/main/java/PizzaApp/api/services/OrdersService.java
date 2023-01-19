package PizzaApp.api.services;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.Order;
import PizzaApp.api.repos.OrdersRepository;
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
}

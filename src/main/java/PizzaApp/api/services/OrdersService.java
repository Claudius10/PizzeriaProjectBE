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
	
	/*
	get orders by store
	public List<Order> findOrdersByStore(Long storeId) {
		
    	// get store
		Store store = em.find(Store.class, storeId);
		
		// get store's orders
		List<Order> storeOrders = store.getOrders();
		 
		// return orders
		return storeOrders;
	}
	*/
}

package PizzaApp.api.repos.order;
import java.util.List;
import PizzaApp.api.entity.order.Order;

public interface OrderRepository {
	
	public void createOrUpdate(Order order);
	
	public Order findById(Long id);
	
	public void deleteById(Long id);
	
	public List<Order> findAll();
	
	public List<Order> findAllByStore(String storeName);
	
	public List<Order> findAllByCustomer(Long customerId);
}

package PizzaApp.api.repos.order;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.EntityManager;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private EntityManager em;

	public OrderRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Order createOrUpdate(Order order) {

		// new order
		if (order.getId() == null) {
			// sync bi associations

			order.setOrderDetails(order.getOrderDetails());
			order.setCart(order.getCart());

			List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
			order.getCart().getOrderItems().clear();

			for (OrderItem item : copy) {
				order.getCart().addItem(item);
			}

			// set user null
			order.setUser(null);
		}

		// return the order
		return em.merge(order);
	}

	@Override
	public Order findById(Long id) {
		return em.find(Order.class, id);
	}

	@Override
	public void deleteById(Long id) {
		// find order
		Order order = findById(id);

		// delete
		em.remove(order);
	}
}

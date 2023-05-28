package PizzaApp.api.repos.order;

import PizzaApp.api.entity.order.Order;

public interface OrderRepository {

	public Order createOrUpdate(Order order);

	public Order findById(Long id);

	public void deleteById(Long id);

}

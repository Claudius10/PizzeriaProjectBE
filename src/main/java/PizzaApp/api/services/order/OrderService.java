package PizzaApp.api.services.order;

import PizzaApp.api.entity.order.Order;

public interface OrderService {

	public Order createOrUpdate(Order order);

	public Order findById(Long id);

	public void deleteById(Long id);

}

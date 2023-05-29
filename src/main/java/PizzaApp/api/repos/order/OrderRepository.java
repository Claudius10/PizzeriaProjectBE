package PizzaApp.api.repos.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderRepository {

	public Order createOrUpdate(Order order);

	public Order findById(Long id);

	public OrderDTO findDTOById(Long id);

	public OrderCreatedOnDTO findCreatedOnById(Long id);

	public void deleteById(Long id);

}

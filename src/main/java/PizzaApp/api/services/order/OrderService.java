package PizzaApp.api.services.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderService {

	public Order createOrUpdate(Order order);

	public Order findById(Long id);

	public OrderDTO findDTOById(Long id);

	public OrderCreatedOnDTO findCreatedOnById(Long id);

	public void deleteById(Long id);

}

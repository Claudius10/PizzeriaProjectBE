package PizzaApp.api.repos.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderRepository {

	Long createOrUpdate(Order order);

	Order findById(Long id);

	OrderDTO findDTOByIdAndTel(String id, String orderContactTel);

	OrderCreatedOnDTO findCreatedOnById(Long id);

	void deleteById(Long id);

}

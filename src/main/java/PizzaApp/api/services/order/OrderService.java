package PizzaApp.api.services.order;

import PizzaApp.api.entity.order.dto.OrderCreatedOnDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderService {

	Long createOrUpdate(Order order);

	Order findById(Long id);

	OrderDTO findDTOByIdAndTel(String id, String orderContactTel);

	OrderCreatedOnDTO findCreatedOnById(Long id);

	void deleteById(Long id);

}

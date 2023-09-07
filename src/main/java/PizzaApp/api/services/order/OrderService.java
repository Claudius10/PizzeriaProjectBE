package PizzaApp.api.services.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderService {

	Long createAnonOrder(Order order);

	Long createUserOrder(UserOrderDTO userOrderDTO);

	OrderDTO findUserOrder(String id);

	Long updateUserOrder(UserOrderDTO userOrderDTO);

	OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber);

	void deleteById(String id);

	// NOTE - for internal use only

	Order findById(String id);

	OrderCreatedOnDTO findCreatedOnById(String id);
}

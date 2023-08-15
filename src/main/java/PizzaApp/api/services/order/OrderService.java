package PizzaApp.api.services.order;

import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;

import java.time.LocalDateTime;

public interface OrderService {

	Long createAnonOrder(Order order);

	Long createUserOrder(UserOrderDTO userOrderDTO);

	OrderDTO findUserOrder(String id);

	OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber);

	Long updateUserOrder(UserOrderDTO userOrderDTO);

	void deleteById(Long id);

	// internal use

	Order findById(Long id);

	LocalDateTime findCreatedOnById(Long id);
}

package PizzaApp.api.repos.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.order.Order;

import java.time.LocalDateTime;

public interface OrderRepository {

	Long createOrder(Order order);

	OrderDTO findUserOrder(String id);

	OrderPaginationResultDTO findOrdersSummary(Long userId, int pageSize, int pageNumber);

	Long updateUserOrder(Order order);

	void deleteById(Long id);

	// internal use

	Order findById(Long id);

	Order findReferenceById(Long id);

	LocalDateTime findCreatedOnById(Long id);
}

package PizzaApp.api.repos.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderSummary;
import PizzaApp.api.entity.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	Long createOrder(Order order);

	OrderDTO findUserOrder(String id);

	Number findUserOrderCount(String userId);

	Optional<List<OrderSummary>> findOrderSummaryList(String userId, int pageSize, int pageNumber);

	Long updateUserOrder(Order order);

	void deleteById(String id);

	// NOTE - for internal use only

	Order findById(String id);

	Order findReferenceById(String id);

	OrderCreatedOnDTO findCreatedOnById(String id);
}

package PizzaApp.api.repos.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderSummary;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.cart.Cart;

import java.util.List;

public interface OrderRepository {

	Long createOrder(Order order);

	OrderDTO findOrder(Long orderId);

	Integer findOrderCount(Long userId);

	List<OrderSummary> findOrderSummaryList(Long userId, int pageSize, int pageNumber);

	Long updateOrder(Order order);

	void deleteById(Long orderId);

	// info - for internal use only

	Order findById(Long orderId);

	void removeUserData(Long userId);

	Order findReferenceById(Long orderId);

	OrderCreatedOnDTO findCreatedOnById(Long orderId);

	Cart findOrderCart(Long orderId);
}

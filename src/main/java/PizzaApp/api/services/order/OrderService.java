package PizzaApp.api.services.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTOPojo;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.order.UserOrderDTO;
import PizzaApp.api.entity.dto.order.UpdateUserOrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.Cart;

import java.time.LocalDateTime;

public interface OrderService {

	String createAnonOrder(Order order);

	String createUserOrder(UserOrderDTO userOrderDTO);

	String updateUserOrder(UpdateUserOrderDTO updateUserOrderDTO);

	String deleteUserOrderById(Long orderId, Long userId);

	OrderDTOPojo findUserOrderDTO(Long orderId);

	OrderPaginationResultDTO findUserOrdersSummary(Long userId, Integer pageSize, Integer pageNumber);

	// info - for internal use only

	Order findById(Long orderId);

	void removeUserData(Long userId);

	Order findReferenceById(Long orderId);

	OrderCreatedOnDTO findCreatedOnById(Long orderId);

	Cart findOrderCart(Long orderId);

	String createUserOrderTest(UserOrderDTO userOrderDTO, LocalDateTime createdOn);
}

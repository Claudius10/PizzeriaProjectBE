package PizzaApp.api.services.order;

import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.NewUserOrderDTO;
import PizzaApp.api.entity.dto.user.UpdateUserOrderDTO;
import PizzaApp.api.entity.order.Order;

import java.time.LocalDateTime;

public interface OrderService {

	Long createAnonOrder(Order order);

	Long createUserOrder(NewUserOrderDTO newUserOrderDTO);

	OrderDTO findUserOrder(Long userId);

	Long updateUserOrder(UpdateUserOrderDTO updateUserOrderDTO);

	OrderPaginationResultDTO findUserOrdersSummary(Long userId, Integer pageSize, Integer pageNumber);

	void deleteUserOrderById(Long orderId);

	// info - for internal use only

	Order findById(Long orderId);

	OrderCreatedOnDTO findCreatedOnById(Long orderId);

	Long createUserOrderTest(NewUserOrderDTO newUserOrderDTO, LocalDateTime createdOn);
}

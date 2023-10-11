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

	OrderDTO findUserOrder(String id);

	Long updateUserOrder(UpdateUserOrderDTO updateUserOrderDTO);

	OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber);

	void deleteById(String id);

	// info - for internal use only

	Order findById(String id);

	OrderCreatedOnDTO findCreatedOnById(String id);

	Long createUserOrderTest(NewUserOrderDTO newUserOrderDTO, LocalDateTime createdOn);
}

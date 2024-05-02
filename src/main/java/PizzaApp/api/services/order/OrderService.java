package PizzaApp.api.services.order;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.dto.*;
import PizzaApp.api.repos.order.projections.OrderSummary;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface OrderService {

	OrderDTO findDTOById(Long orderId);

	CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder);

	Long createUserOrder(NewUserOrderDTO newUserOrder);

	Long updateUserOrder(UpdateUserOrderDTO updateUserOrder);

	Long deleteUserOrderById(Long orderId);

	Page<OrderSummary> findUserOrderSummary(Long userId, int size, int page);

	// info - for internal use only

	Order findUserOrderById(Long orderId);

	LocalDateTime findCreatedOnById(Long orderId);
}

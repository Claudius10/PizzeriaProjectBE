package org.pizzeria.api.services.order;

import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.entity.order.dto.*;
import org.pizzeria.api.repos.order.projections.OrderSummary;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {

	Optional<OrderDTO> findDTOById(Long orderId);

	CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder);

	Long createUserOrder(NewUserOrderDTO newUserOrder);

	Long updateUserOrder(UpdateUserOrderDTO updateUserOrder);

	Long deleteUserOrderById(Long orderId);

	Page<OrderSummary> findUserOrderSummary(Long userId, int size, int page);

	// info - for internal use only

	Optional<Order> findUserOrderById(Long orderId);

	LocalDateTime findCreatedOnById(Long orderId);
}
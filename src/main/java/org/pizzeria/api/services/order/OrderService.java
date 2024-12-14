package org.pizzeria.api.services.order;

import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.web.dto.order.dto.*;
import org.pizzeria.api.web.dto.order.projection.OrderSummaryProjection;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {

	Optional<OrderDTO> findProjectionById(Long orderId);

	CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder);

	Long createUserOrder(Long orderId, NewUserOrderDTO newUserOrder);

	boolean updateUserOrder(Long orderId, UpdateUserOrderDTO updateUserOrder);

	void deleteUserOrderById(Long orderId);

	Page<OrderSummaryProjection> findUserOrderSummary(Long userId, int size, int page);

	// info - for internal use only

	Optional<Order> findUserOrderById(Long orderId);

	LocalDateTime findCreatedOnById(Long orderId);
}
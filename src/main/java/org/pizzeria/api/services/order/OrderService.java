package org.pizzeria.api.services.order;

import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.entity.order.dto.CreatedAnonOrderDTO;
import org.pizzeria.api.entity.order.dto.NewAnonOrderDTO;
import org.pizzeria.api.entity.order.dto.NewUserOrderDTO;
import org.pizzeria.api.entity.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.entity.order.dto.OrderDTO;
import org.pizzeria.api.entity.order.projections.OrderSummaryProjection;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {

	Optional<OrderDTO> findProjectionById(Long orderId);

	CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder);

	Long createUserOrder(Long orderId, NewUserOrderDTO newUserOrder);

	Long updateUserOrder(Long orderId, UpdateUserOrderDTO updateUserOrder);

	Long deleteUserOrderById(Long orderId);

	Page<OrderSummaryProjection> findUserOrderSummary(Long userId, int size, int page);

	// info - for internal use only

	Optional<Order> findUserOrderById(Long orderId);

	LocalDateTime findCreatedOnById(Long orderId);
}
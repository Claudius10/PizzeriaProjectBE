package org.pizzeria.api.repos.order;

import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.repos.order.projections.CreatedOnOnly;
import org.pizzeria.api.repos.order.projections.OrderSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("from Order order " +
			"join fetch order.orderDetails " +
			"join fetch order.address " +
			"join fetch order.cart as cart " +
			"join fetch cart.orderItems " +
			"left join fetch order.user " +
			"where order.id = :orderId")
	Optional<Order> findUserOrderById(Long orderId);

	Page<OrderSummary> findAllByUser_Id(Long userId, Pageable pageable);

	// internal use only

	Optional<CreatedOnOnly> findCreatedOnById(Long orderId);
}
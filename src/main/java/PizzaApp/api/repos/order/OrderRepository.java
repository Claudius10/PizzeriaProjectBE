package PizzaApp.api.repos.order;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.projections.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("from Order order " +
			"join fetch order.orderDetails " +
			"join fetch order.address " +
			"join fetch order.cart as cart " +
			"join fetch cart.orderItems " +
			"left join fetch order.user " +
			"where order.id = :orderId")
	Order findByIdNoLazy(Long orderId);

	Page<OrderSummary> findAllByUser_Id(Long userId, Pageable pageable);

	// internal use only

	CreatedOnOnly findCreatedOnById(Long orderId);
}

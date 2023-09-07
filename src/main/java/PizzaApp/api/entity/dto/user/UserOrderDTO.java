package PizzaApp.api.entity.dto.user;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;

import java.time.LocalDateTime;

public record UserOrderDTO(
		Long orderId,
		LocalDateTime createdOn,
		String formattedCreatedOn,
		UserOrderData userOrderData,
		OrderDetails orderDetails,
		Cart cart) {
}

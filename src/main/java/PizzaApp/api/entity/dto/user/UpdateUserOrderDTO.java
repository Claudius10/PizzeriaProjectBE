package PizzaApp.api.entity.dto.user;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;

import java.time.LocalDateTime;

public record UpdateUserOrderDTO(
		Long orderId,
		LocalDateTime createdOn,
		String formattedCreatedOn,
		UserOrderDataDTO userOrderData,
		OrderDetails orderDetails,
		Cart cart) {
}

package PizzaApp.api.entity.dto.user;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;

public record NewUserOrderDTO(
		UserOrderData userOrderData,
		OrderDetails orderDetails,
		Cart cart) {
}

package PizzaApp.api.entity.dto.order;

import PizzaApp.api.entity.dto.user.UserOrderDataDTO;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;

public record UserOrderDTO(
		UserOrderDataDTO userOrderData,
		OrderDetails orderDetails,
		Cart cart) {
}

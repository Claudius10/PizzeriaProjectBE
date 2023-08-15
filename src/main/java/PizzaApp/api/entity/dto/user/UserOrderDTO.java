package PizzaApp.api.entity.dto.user;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;

public record UserOrderDTO(Long orderId, UserOrderData userOrderData, OrderDetails orderDetails, Cart cart) {
}

package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;

public record NewUserOrderDTO(

		Long userId,

		Long addressId,

		OrderDetails orderDetails,

		Cart cart) {
}

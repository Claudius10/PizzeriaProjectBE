package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.Cart;

public record NewUserOrderDTO(

		Long userId,

		Long addressId,

		OrderDetails orderDetails,

		Cart cart) {
}

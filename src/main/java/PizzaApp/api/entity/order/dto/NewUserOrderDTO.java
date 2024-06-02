package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import jakarta.validation.constraints.NotNull;

public record NewUserOrderDTO(

		@NotNull
		Long userId,

		@NotNull
		Long addressId,

		OrderDetails orderDetails,

		Cart cart) {
}
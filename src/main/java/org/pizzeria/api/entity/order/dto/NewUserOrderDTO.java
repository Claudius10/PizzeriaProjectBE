package org.pizzeria.api.entity.order.dto;

import jakarta.validation.constraints.NotNull;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

public record NewUserOrderDTO(
		@NotNull
		Long addressId,

		OrderDetails orderDetails,

		Cart cart) {
}
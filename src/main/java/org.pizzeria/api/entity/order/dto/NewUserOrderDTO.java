package org.pizzeria.api.entity.order.dto;

import jakarta.validation.constraints.NotNull;
import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

public record NewUserOrderDTO(

		@NotNull
		Long userId,

		@NotNull
		Long addressId,

		OrderDetails orderDetails,

		Cart cart) {
}
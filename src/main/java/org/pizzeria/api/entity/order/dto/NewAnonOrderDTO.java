package org.pizzeria.api.entity.order.dto;

import jakarta.validation.Valid;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

public record NewAnonOrderDTO(

		@Valid
		CustomerDTO customer,

		@Valid
		Address address,

		@Valid
		OrderDetails orderDetails,

		Cart cart
) {
}
package org.pizzeria.api.web.dto.order.dto;

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
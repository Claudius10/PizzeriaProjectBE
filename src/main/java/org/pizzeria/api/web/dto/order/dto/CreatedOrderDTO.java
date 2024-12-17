package org.pizzeria.api.web.dto.order.dto;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

public record CreatedOrderDTO(

		Long id,

		String formattedCreatedOn,

		CustomerDTO customer,

		Address address,

		OrderDetails orderDetails,

		Cart cart) {
}
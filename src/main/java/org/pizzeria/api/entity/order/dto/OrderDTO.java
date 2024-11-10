package org.pizzeria.api.entity.order.dto;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

public record OrderDTO(
		Long id,
		LocalDateTime createdOn,
		LocalDateTime updatedOn,
		String formattedCreatedOn,
		String formattedUpdatedOn,
		Address address,
		OrderDetails orderDetails,
		Cart cart
) {
}
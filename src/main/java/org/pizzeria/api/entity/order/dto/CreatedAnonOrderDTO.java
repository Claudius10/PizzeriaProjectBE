package org.pizzeria.api.entity.order.dto;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

public record CreatedAnonOrderDTO(

		Long id,

		String formattedCreatedOn,

		String anonCustomerName,

		Integer anonCustomerContactNumber,

		String anonCustomerEmail,

		Address address,

		OrderDetails orderDetails,

		Cart cart) {
}
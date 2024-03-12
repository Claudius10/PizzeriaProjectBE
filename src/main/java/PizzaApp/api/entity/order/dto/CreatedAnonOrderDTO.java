package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;

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

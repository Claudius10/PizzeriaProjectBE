package PizzaApp.api.entity.dto.order;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.address.Address;

import java.time.LocalDateTime;

public record OrderDTO(
		Long id,
		LocalDateTime createdOn,
		LocalDateTime updatedOn,
		String formattedCreatedOn,
		String formattedUpdatedOn,
		String customerName,
		Integer contactTel,
		String email,
		Address address,
		OrderDetails orderDetails,
		Cart cart
) {
}

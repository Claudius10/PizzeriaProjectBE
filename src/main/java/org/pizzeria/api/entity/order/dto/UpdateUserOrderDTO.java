package org.pizzeria.api.entity.order.dto;

import jakarta.validation.constraints.NotNull;
import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

public class UpdateUserOrderDTO {

	@NotNull(message = "El valor del ID de la dirección no puede ser null.")
	private final Long addressId;

	@NotNull(message = "El valor de la fecha de creación del pedido no puede ser null.")
	private final LocalDateTime createdOn;

	@NotNull(message = "Los detalles del pedido (orderDetails) no pueden ser null.")
	private final OrderDetails orderDetails;

	private Cart cart;

	public UpdateUserOrderDTO(
			Long addressId,
			LocalDateTime createdOn,
			OrderDetails orderDetails,
			Cart cart) {
		this.addressId = addressId;
		this.createdOn = createdOn;
		this.orderDetails = orderDetails;
		this.cart = cart;
	}

	public Long getAddressId() {
		return addressId;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
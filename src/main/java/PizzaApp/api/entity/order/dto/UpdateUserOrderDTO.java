package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateUserOrderDTO {

	@NotNull(message = "El valor del ID del pedido no puede ser null.")
	private final Long orderId;

	@NotNull(message = "El valor del ID del usuario no puede ser null.")
	private final Long userId;

	@NotNull(message = "El valor del ID de la dirección no puede ser null.")
	private final Long addressId;

	@NotNull(message = "El valor de la fecha de creación del pedido no puede ser null.")
	private final LocalDateTime createdOn;

	@NotNull(message = "Los detalles del pedido (orderDetails) no pueden ser null.")
	private final OrderDetails orderDetails;

	private Cart cart;

	public UpdateUserOrderDTO(
			Long orderId,
			Long userId,
			Long addressId,
			LocalDateTime createdOn,
			OrderDetails orderDetails,
			Cart cart) {
		this.orderId = orderId;
		this.userId = userId;
		this.addressId = addressId;
		this.createdOn = createdOn;
		this.orderDetails = orderDetails;
		this.cart = cart;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getUserId() {
		return userId;
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
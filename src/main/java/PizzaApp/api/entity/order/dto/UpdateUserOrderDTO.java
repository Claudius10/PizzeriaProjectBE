package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

public class UpdateUserOrderDTO {

	private final Long orderId;

	private final Long userId;

	private final Long addressId;

	private final LocalDateTime createdOn;

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
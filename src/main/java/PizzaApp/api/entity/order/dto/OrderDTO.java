package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

public final class OrderDTO {

	private final Long id;

	private final LocalDateTime createdOn;

	private final LocalDateTime updatedOn;

	private final String formattedCreatedOn;

	private final String formattedUpdatedOn;

	private final Address address;

	private final OrderDetails orderDetails;

	private final Cart cart;

	private final UserDTO user;

	public OrderDTO(
			Long id,
			LocalDateTime createdOn,
			LocalDateTime updatedOn,
			String formattedCreatedOn,
			String formattedUpdatedOn,
			Address address,
			OrderDetails orderDetails,
			Cart cart,
			UserDTO user) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.formattedCreatedOn = formattedCreatedOn;
		this.formattedUpdatedOn = formattedUpdatedOn;
		this.address = address;
		this.orderDetails = orderDetails;
		this.cart = cart;
		this.user = user;
	}

	public OrderDTO(Order order) {
		this.id = order.getId();
		this.createdOn = order.getCreatedOn();
		this.updatedOn = order.getUpdatedOn();
		this.formattedCreatedOn = order.getFormattedCreatedOn();
		this.formattedUpdatedOn = order.getFormattedUpdatedOn();
		this.address = order.getAddress();
		this.orderDetails = order.getOrderDetails();
		this.cart = order.getCart();
		if (order.getUser() != null) {
			this.user = new UserDTO(
					order.getUser().getId(),
					order.getUser().getName(),
					order.getUser().getUsername(),
					order.getUser().getContactNumber());
		} else {
			this.user = null;
		}
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public String getFormattedCreatedOn() {
		return formattedCreatedOn;
	}

	public String getFormattedUpdatedOn() {
		return formattedUpdatedOn;
	}

	public Address getAddress() {
		return address;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public Cart getCart() {
		return cart;
	}

	public UserDTO getUser() {
		return user;
	}
}
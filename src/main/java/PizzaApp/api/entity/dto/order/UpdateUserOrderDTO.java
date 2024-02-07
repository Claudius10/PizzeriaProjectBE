package PizzaApp.api.entity.dto.order;

import PizzaApp.api.entity.dto.user.UserOrderDataDTO;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.Cart;

import java.time.LocalDateTime;

public class UpdateUserOrderDTO {

	private Long orderId;

	private LocalDateTime createdOn;

	private String formattedCreatedOn;

	private UserOrderDataDTO userOrderData;

	private OrderDetails orderDetails;

	private Cart cart;

	public UpdateUserOrderDTO(Long orderId, LocalDateTime createdOn, String formattedCreatedOn, UserOrderDataDTO userOrderData, OrderDetails orderDetails, Cart cart) {
		this.orderId = orderId;
		this.createdOn = createdOn;
		this.formattedCreatedOn = formattedCreatedOn;
		this.userOrderData = userOrderData;
		this.orderDetails = orderDetails;
		this.cart = cart;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getFormattedCreatedOn() {
		return formattedCreatedOn;
	}

	public void setFormattedCreatedOn(String formattedCreatedOn) {
		this.formattedCreatedOn = formattedCreatedOn;
	}

	public UserOrderDataDTO getUserOrderData() {
		return userOrderData;
	}

	public void setUserOrderData(UserOrderDataDTO userOrderData) {
		this.userOrderData = userOrderData;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}

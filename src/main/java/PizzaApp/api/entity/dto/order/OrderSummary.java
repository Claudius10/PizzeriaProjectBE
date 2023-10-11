package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

public class OrderSummary {

	private Long id;

	private LocalDateTime createdOn, updatedOn;

	private String formattedCreatedOn, formattedUpdatedOn;

	private OrderDetailsDTO orderDetails;

	private CartDTO cart;

	public OrderSummary(
			Long id,
			LocalDateTime createdOn,
			LocalDateTime updatedOn,
			String formattedCreatedOn,
			String formattedUpdatedOn,
			OrderDetailsDTO orderDetailsDTO,
			CartDTO cartDTO) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.formattedCreatedOn = formattedCreatedOn;
		this.formattedUpdatedOn = formattedUpdatedOn;
		this.orderDetails = orderDetailsDTO;
		this.cart = cartDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getFormattedCreatedOn() {
		return formattedCreatedOn;
	}

	public void setFormattedCreatedOn(String formattedCreatedOn) {
		this.formattedCreatedOn = formattedCreatedOn;
	}

	public String getFormattedUpdatedOn() {
		return formattedUpdatedOn;
	}

	public void setFormattedUpdatedOn(String formattedUpdatedOn) {
		this.formattedUpdatedOn = formattedUpdatedOn;
	}

	public OrderDetailsDTO getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetailsDTO orderDetails) {
		this.orderDetails = orderDetails;
	}

	public CartDTO getCart() {
		return cart;
	}

	public void setCart(CartDTO cartDTO) {
		this.cart = cartDTO;
	}
}

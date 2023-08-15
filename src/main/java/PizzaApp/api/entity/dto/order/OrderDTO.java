package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.user.Address;

// Order entity without User
// for fetching to front end without User data

public class OrderDTO {

	private Long id;

	private LocalDateTime createdOn;

	private LocalDateTime updatedOn;

	private String formattedCreatedOn;

	private String formattedUpdatedOn;

	private String customerName;

	private Integer contactTel;

	private String email;

	private Address address;

	private OrderDetails orderDetails;

	private Cart cart;


	public OrderDTO(Long id, LocalDateTime createdOn, LocalDateTime updatedOn, String customerName, Integer contactTel, String email, Address address, OrderDetails orderDetails, Cart cart) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.customerName = customerName;
		this.contactTel = contactTel;
		this.email = email;
		this.address = address;
		this.orderDetails = orderDetails;
		this.cart = cart;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getContactTel() {
		return contactTel;
	}

	public void setContactTel(Integer contactTel) {
		this.contactTel = contactTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

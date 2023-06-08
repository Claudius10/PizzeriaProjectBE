package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.order.OrderDetails;

// Order entity without User
// for fetching to front end without User data

public class OrderDTO {

	private Long id;

	private LocalDateTime createdOn;

	private LocalDateTime updatedOn;

	private String formattedCreatedOn;

	private String formattedUpdatedOn;

	private String customerFirstName;

	private String customerLastName;

	private Integer contactTel;

	private Address address;

	private Email email;

	private OrderDetails orderDetails;

	private Cart cart;

	public OrderDTO(Long id, LocalDateTime createdOn, LocalDateTime updatedOn, String customerFirstName,
					String customerLastName, Integer contactTel, Address address, Email email, OrderDetails orderDetails,
					Cart cart) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.contactTel = contactTel;
		this.address = address;
		this.email = email;
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

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public Integer getContactTel() {
		return contactTel;
	}

	public void setContactTel(Integer contactTel) {
		this.contactTel = contactTel;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
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

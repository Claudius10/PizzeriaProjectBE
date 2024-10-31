package org.pizzeria.api.entity.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "Order")
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
	@SequenceGenerator(name = "order_generator", sequenceName = "order_seq", allocationSize = 1)
	private Long id;

	@Column
	private LocalDateTime createdOn;

	@Column
	private LocalDateTime updatedOn;

	@Column
	private String formattedCreatedOn;

	@Column
	private String formattedUpdatedOn;

	@Column
	private String anonCustomerName;

	@Column
	private Integer anonCustomerContactNumber;

	@Column
	private String anonCustomerEmail;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
			fetch = FetchType.LAZY)
	private Address address;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JsonManagedReference
	private OrderDetails orderDetails;

	// NOTE - bidirectional OneToOne association's non-owning side
	//  can only be lazy fetched if the association is never null ->
	//  optional = false
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JsonManagedReference
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private User user;

	public Order() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		if (orderDetails == null) {
			if (this.orderDetails != null) {
				this.orderDetails.setOrder(null);
			}
		} else {
			orderDetails.setOrder(this);
		}
		this.orderDetails = orderDetails;
	}

	public void setCart(Cart cart) {
		if (cart == null) {
			if (this.cart != null) {
				this.cart.setOrder(null);
			}
		} else {
			cart.setOrder(this);
		}
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

	public String getAnonCustomerName() {
		return anonCustomerName;
	}

	public void setAnonCustomerName(String anonCustomerName) {
		this.anonCustomerName = anonCustomerName;
	}

	public Integer getAnonCustomerContactNumber() {
		return anonCustomerContactNumber;
	}

	public void setAnonCustomerContactNumber(Integer anonCustomerContactNumber) {
		this.anonCustomerContactNumber = anonCustomerContactNumber;
	}

	public String getAnonCustomerEmail() {
		return anonCustomerEmail;
	}

	public void setAnonCustomerEmail(String anonCustomerEmail) {
		this.anonCustomerEmail = anonCustomerEmail;
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

	public Cart getCart() {
		return cart;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static class Builder {

		private final Order order;

		public Builder() {
			order = new Order();
		}

		public Builder withId(long id) {
			order.id = id;
			return this;
		}

		public Builder withCreatedOn(LocalDateTime createdOn) {
			order.createdOn = createdOn;
			return this;
		}

		public Builder withFormattedCreatedOn(String formattedCreatedOn) {
			order.formattedCreatedOn = formattedCreatedOn;
			return this;
		}

		public Builder withFormattedUpdatedOn(String formattedUpdatedOn) {
			order.formattedUpdatedOn = formattedUpdatedOn;
			return this;
		}

		public Builder withUpdatedOn(LocalDateTime updatedOn) {
			order.updatedOn = updatedOn;
			return this;
		}

		public Builder withAddress(Address address) {
			order.address = address;
			return this;
		}

		public Builder withOrderDetails(OrderDetails orderDetails) {
			order.setOrderDetails(orderDetails);
			return this;
		}

		public Builder withCart(Cart cart) {
			order.setCart(cart);
			return this;
		}

		public Builder withAnonCustomer(String anonCustomerName, Integer anonCustomerContactNumber, String anonCustomerEmail) {
			order.anonCustomerName = anonCustomerName;
			order.anonCustomerContactNumber = anonCustomerContactNumber;
			order.anonCustomerEmail = anonCustomerEmail;
			order.user = null;
			return this;
		}

		public Builder withUser(User user) {
			order.anonCustomerName = null;
			order.anonCustomerContactNumber = null;
			order.anonCustomerEmail = null;
			order.user = user;
			return this;
		}

		public Order build() {
			return order;
		}
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Order))
			return false;

		return id != null && id.equals(((Order) obj).getId());
	}

	public boolean contentEquals(Object o) {
		Order order = (Order) o;
		return Objects.equals(createdOn, order.createdOn) &&
				Objects.equals(updatedOn, order.updatedOn) &&
				Objects.equals(anonCustomerName, order.getAnonCustomerName()) &&
				Objects.equals(anonCustomerContactNumber, order.getAnonCustomerContactNumber()) &&
				Objects.equals(anonCustomerEmail, order.getAnonCustomerEmail()) &&
				this.address.contentEquals(order.address) &&
				this.orderDetails.contentEquals(order.orderDetails) &&
				this.cart.contentEquals(order.cart);
	}
}
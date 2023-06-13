package PizzaApp.api.entity.order;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.clients.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Entity(name = "Order")
@Table(name = "customer_order")
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "c_first_name")
	@Pattern(regexp = "^[a-zA-Z\s]{2,25}$", message = "Nombre: solo letras sin tildes (mín 2, máx 25 letras)")
	private String customerFirstName;

	@Column(name = "c_last_name")
	@Pattern(regexp = "^[a-zA-Z\s]{0,25}$", message = "Apellido: solo letras sin tildes (no mín, máx 25 letras)")
	private String customerLastName;

	/*
	for contactTel there can't be field validation
	cause when receiving Order obj for update without
	/contactTel, it will insta reject before being able
	to do logic in orderServiceImpl
	*/
	@Column(name = "contact_tel")
	private Integer contactTel;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Valid
	private Email email;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Valid
	private Address address;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@Valid
	private OrderDetails orderDetails;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public Order() {
	}

	private Order(Builder builder) {
		this.id = builder.id;
		this.createdOn = builder.createdOn;
		this.updatedOn = builder.updatedOn;
		this.customerFirstName = builder.customerFirstName;
		this.customerLastName = builder.customerLastName;
		this.contactTel = builder.contactTel;
		this.email = builder.email;
		this.address = builder.address;
		this.orderDetails = builder.orderDetails;
		this.cart = builder.cart;
		this.user = null;
	}

	public static class Builder {
		private Long id;
		private LocalDateTime createdOn;
		private LocalDateTime updatedOn;
		private String customerFirstName;
		private String customerLastName;
		private Integer contactTel;
		private Email email;
		private Address address;
		private OrderDetails orderDetails;
		private Cart cart;

		public Builder() {
		}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withCreatedOn(LocalDateTime createdOn) {
			this.createdOn = createdOn;
			return this;
		}

		public Builder withUpdatedOn(LocalDateTime updatedOn) {
			this.updatedOn = updatedOn;
			return this;
		}

		public Builder withCustomerFirstName(String customerFirstName) {
			this.customerFirstName = customerFirstName;
			return this;
		}

		public Builder withCustomerLastName(String customerLastName) {
			this.customerLastName = customerLastName;
			return this;
		}

		public Builder withContactTel(Integer contactTel) {
			this.contactTel = contactTel;
			return this;
		}

		public Builder withEmail(Email email) {
			this.email = email;
			return this;
		}

		public Builder withAddress(Address address) {
			this.address = address;
			return this;
		}

		public Builder withOrderDetails(OrderDetails orderDetails) {
			this.orderDetails = orderDetails;
			return this;
		}

		public Builder withCart(Cart cart) {
			this.cart = cart;
			return this;
		}

		public Order build() {
			return new Order(this);
		}
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

	public Cart getCart() {
		return cart;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	@Override
	public String toString() {
		return "Order [id=" + id + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", customerFirstName="
				+ customerFirstName + ", customerLastName=" + customerLastName + ", contactTel=" + contactTel
				+ ", address=" + address + ", email=" + email + "]";
	}

	public boolean entityEquals(Object o) {
		Order order = (Order) o;

		// orderItem contentEquals
		boolean equalContentOfOrderItems = false;
		if (order.getCart() != null) {
			for (OrderItem item : cart.getOrderItems()) {
				for (OrderItem otherItem : order.cart.getOrderItems()) {
					if (item.contentEquals(otherItem)) {
						equalContentOfOrderItems = true;
					}
				}
			}
		} else {
			equalContentOfOrderItems = true;
		}

		return Objects.equals(createdOn, order.createdOn) &&
				Objects.equals(updatedOn, order.updatedOn) &&

				Objects.equals(customerFirstName, order.customerFirstName) &&
				Objects.equals(customerLastName, order.customerLastName) &&

				Objects.equals(contactTel, order.contactTel) &&
				Objects.equals(email.getEmail(), order.email.getEmail()) &&

				Objects.equals(address.getStreet(), order.address.getStreet()) &&
				Objects.equals(address.getStreetNr(), order.address.getStreetNr()) &&
				Objects.equals(address.getStaircase(), order.address.getStaircase()) &&
				Objects.equals(address.getGate(), order.address.getGate()) &&
				Objects.equals(address.getFloor(), order.address.getFloor()) &&
				Objects.equals(address.getDoor(), order.address.getDoor()) &&

				Objects.equals(orderDetails.getDeliveryHour(), order.orderDetails.getDeliveryHour()) &&
				Objects.equals(orderDetails.getPaymentType(), order.orderDetails.getPaymentType()) &&
				Objects.equals(orderDetails.getChangeRequested(), order.orderDetails.getChangeRequested()) &&
				Objects.equals(orderDetails.getPaymentChange(), order.orderDetails.getPaymentChange()) &&
				Objects.equals(orderDetails.getDeliveryComment(), order.orderDetails.getDeliveryComment()) &&

				Objects.equals(cart.getTotalQuantity(), order.cart.getTotalQuantity()) &&
				Objects.equals(cart.getTotalCost(), order.cart.getTotalCost()) &&
				Objects.equals(cart.getTotalCostOffers(), order.cart.getTotalCostOffers()) &&
				equalContentOfOrderItems;
	}
}
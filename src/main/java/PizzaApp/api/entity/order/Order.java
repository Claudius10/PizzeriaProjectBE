package PizzaApp.api.entity.order;

import java.time.LocalDateTime;
import java.util.Objects;

import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.entity.common.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.order.cart.Cart;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

	@Column(name = "c_name")
	@Pattern(regexp = "^[a-zA-Z\s]{2,50}$",
			message = "Nombre / apellidos: solo letras sin tildes (mín 2, máx 25 letras)")
	private String customerName;

	/* for contactTel there can't be field validation cause when receiving Order obj for update without
	contactTel, it will insta reject before being able to do logic in orderServiceImpl */
	@Column(name = "contact_tel")
	private Integer contactTel;

	/* same as contactTel for validation */
	@Column(name = "email")
	@Email(message = "El formato del email no es aceptado")
	private String email;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Valid
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userdata_id")
	@JsonBackReference
	private UserData userData;

	@OneToOne(mappedBy = "order",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonManagedReference
	@Valid
	private OrderDetails orderDetails;

	@OneToOne(mappedBy = "order",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonManagedReference
	private Cart cart;

	public Order() {
	}

	private Order(Builder builder) {
		this.id = builder.id;
		this.createdOn = builder.createdOn;
		this.updatedOn = builder.updatedOn;
		this.customerName = builder.customerName;
		this.contactTel = builder.contactTel;
		this.email = builder.email;
		this.address = builder.address;
		this.orderDetails = builder.orderDetails;
		this.cart = builder.cart;
		this.userData = null;
	}

	public static class Builder {
		private Long id;
		private LocalDateTime createdOn;
		private LocalDateTime updatedOn;
		private String customerName;
		private Integer contactTel;
		private String email;
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

		public Builder withCustomerName(String customerName) {
			this.customerName = customerName;
			return this;
		}


		public Builder withContactTel(Integer contactTel) {
			this.contactTel = contactTel;
			return this;
		}

		public Builder withEmail(String email) {
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public Cart getCart() {
		return cart;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
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

				Objects.equals(customerName, order.customerName) &&
				Objects.equals(contactTel, order.contactTel) &&
				Objects.equals(email, order.getEmail()) &&

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
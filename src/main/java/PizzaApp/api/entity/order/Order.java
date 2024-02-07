package PizzaApp.api.entity.order;

import java.time.LocalDateTime;
import java.util.Objects;

import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.exceptions.constraints.IntegerLength;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

	@Column(name = "formatted_created_on")
	private String formattedCreatedOn;

	@Column(name = "formatted_updated_on")
	private String formattedUpdatedOn;

	@Column(name = "c_name")
	@Pattern(regexp = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$",
			message = "Compruebe que el nombre y apellido(s) esté compuesto solo por un mínimo de 2 y un máximo de 50 letras")
	private String customerName;

	@Column(name = "contact_tel")
	@IntegerLength(min = 9, max = 9, message = "Compruebe que el número de teléfono tenga 9 digitos")
	private Integer contactTel;

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
			cascade = CascadeType.ALL)
	@JsonManagedReference
	@Valid
	private OrderDetails orderDetails;

	@OneToOne(mappedBy = "order",
			cascade = CascadeType.ALL)
	@JsonManagedReference
	private Cart cart;

	public Order() {
	}

	private Order(Builder builder) {
		this.id = builder.id;
		this.createdOn = builder.createdOn;
		this.updatedOn = builder.updatedOn;
		this.formattedCreatedOn = builder.formattedCreatedOn;
		this.formattedUpdatedOn = builder.formattedUpdatedOn;
		this.customerName = builder.customerName;
		this.contactTel = builder.contactTel;
		this.email = builder.email;
		this.address = builder.address;
		this.orderDetails = builder.orderDetails;
		this.cart = builder.cart;
		this.userData = builder.userData;
	}

	public static class Builder {
		private Long id;

		private LocalDateTime createdOn, updatedOn;

		private String customerName, email, formattedCreatedOn, formattedUpdatedOn;

		private Integer contactTel;

		private Address address;

		private OrderDetails orderDetails;

		private Cart cart;

		private UserData userData;

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

		public Builder withFormattedCreatedOn(String formattedCreatedOn) {
			this.formattedCreatedOn = formattedCreatedOn;
			return this;
		}

		public Builder withFormattedUpdatedOn(String formattedUpdatedOn) {
			this.formattedUpdatedOn = formattedUpdatedOn;
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

		public Builder withUser(UserData user) {
			this.userData = user;
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
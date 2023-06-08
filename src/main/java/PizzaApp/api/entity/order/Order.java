package PizzaApp.api.entity.order;

import java.time.LocalDateTime;

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

@Entity
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

	@Column(name = "c_first_name", nullable = true)
	@Pattern(regexp = "^[a-zA-Z\s]{2,25}$", message = "Nombre: solo letras sin tildes (mín 2, máx 25 letras)")
	private String customerFirstName;

	@Column(name = "c_last_name", nullable = true)
	@Pattern(regexp = "^[a-zA-Z\s]{0,25}$", message = "Apellido: solo letras sin tildes (no mín, máx 25 letras)")
	private String customerLastName;

	// for contactTel there can't be field validation
	// cause when receiving Order obj for update without
	// contactTel, it will insta reject before being able
	// to do logic in orderServiceImpl
	@Column(name = "contact_tel", nullable = true)
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

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private User user;

	public Order() {
	}

	public Order(Long id, LocalDateTime createdOn, LocalDateTime updatedOn, String customerFirstName,
				 String customerLastName, Integer contactTel, Email email, Address address, OrderDetails orderDetails,
				 Cart cart, User user) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.contactTel = contactTel;
		this.email = email;
		this.address = address;
		this.orderDetails = orderDetails;
		this.cart = cart;
		this.user = user;
	}

	public Order(LocalDateTime createdOn, LocalDateTime updatedOn, String customerFirstName, String customerLastName,
				 Integer contactTel, Email email, Address address, OrderDetails orderDetails, Cart cart, User user) {
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.contactTel = contactTel;
		this.email = email;
		this.address = address;
		this.orderDetails = orderDetails;
		this.cart = cart;
		this.user = user;
	}

	public Order(LocalDateTime createdOn, LocalDateTime updatedOn, String customerFirstName, String customerLastName,
				 Integer contactTel, Email email, Address address, OrderDetails orderDetails, Cart cart) {
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.contactTel = contactTel;
		this.email = email;
		this.address = address;
		this.orderDetails = orderDetails;
		this.cart = cart;
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
}
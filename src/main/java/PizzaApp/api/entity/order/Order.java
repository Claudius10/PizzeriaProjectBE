package PizzaApp.api.entity.order;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.customer.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "order")
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Order FK references OrderDetails PK
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "details_id")
	@Valid
	private OrderDetails orderDetails;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinColumn(name = "address_id")
	@Valid
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@Column(name = "store_pickup_name")
	private String storePickUpName;

	// FK in customer_order(this) references customer PK
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinColumn(name = "customer_id")
	@Valid
	private Customer customer;

	public Order() {
	}

	public Order(Long id, OrderDetails orderDetails, Address address, Cart cart, String storePickUpName,
			Customer customer) {
		this.id = id;
		this.orderDetails = orderDetails;
		this.address = address;
		this.cart = cart;
		this.storePickUpName = storePickUpName;
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getStorePickUpName() {
		return storePickUpName;
	}

	public void setStorePickUpName(String storePickUpName) {
		this.storePickUpName = storePickUpName;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDetails=" + orderDetails + ", address=" + address + ", cart=" + cart
				+ ", storePickUpName=" + storePickUpName + ", customer=" + customer + "]";
	}
}

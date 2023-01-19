package PizzaApp.api.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_order")
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// FK in customer_order(this) references customer PK
	@ManyToOne(cascade = jakarta.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
	// EAGER fetching (default) for ManyToOne association would result in bad
	// performance.
	// The bidirectional association requires the child entity mapping to provide
	// a @ManyToOne annotation, which is responsible for controlling the
	// association.
	// The @ManyToOne annotation allows you to map the Foreign Key column in the
	// child entity mapping so that the child has a reference to its parent entity.
	//
	// unidirectional @OneToMany association is simpler since it’s just the
	// parent-side that defines the relationship
	@JoinColumn(name = "customer_id")
	// the Many side of OneToMany / ManyToOne BI relationship must be the owning
	// side
	// JoinColumn defines that the orders table is the owning side of the
	// relationship meaning it holds and manages the FK column
	private Customer customer;

	// FK in customer_order(this) references address PK
	@OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	// FK in customer_order(this) references customer_order_details PK
	// Order FK references OrderDetails PK
	@OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
	@JoinColumn(name = "details_id")
	private OrderDetails orderDetails;

	// Order PK is referenced by OrderItem FK
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderItem> orderItems = new ArrayList<>();

	public Order() {
	}

	public Order(Long id, Customer customer, Address address, OrderDetails orderDetails, List<OrderItem> orderItems) {
		this.id = id;
		this.customer = customer;
		this.address = address;
		this.orderDetails = orderDetails;
		this.orderItems = orderItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", address=" + address + ", orderDetails=" + orderDetails
				+ ", orderItems=" + orderItems + "]";
	}
}

package PizzaApp.api.entity.cart;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	private Long id;

	@Column(name = "total_quantity")
	private int totalQuantity;

	@Column(name = "total_cost")
	private Double totalCost;

	@Column(name = "total_cost_offers")
	private Double totalCostOffers;

	// NOTE to remember about the Cart/OrderItem association:
	// given that Order & Cart association has CascadeType.ALL
	// and Cart & OrderItem bidirectional association also has CascadeType.ALL
	// when updating Cart, the merge operation is going to be cascaded to the
	// OrderItem association as well, so there's no need to manually
	// sync the bidirectional association
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne
	@MapsId
	@JsonBackReference
	private Order order;

	public Cart() {
	}

	public Cart(Long id, int totalQuantity, Double totalCost, Double totalCostOffers, List<OrderItem> orderItems,
			Order order) {
		this.id = id;
		this.totalQuantity = totalQuantity;
		this.totalCost = totalCost;
		this.totalCostOffers = totalCostOffers;
		this.orderItems = orderItems;
		this.order = order;
	}

	public Cart(int totalQuantity, Double totalCost, Double totalCostOffers, List<OrderItem> orderItems, Order order) {
		this.totalQuantity = totalQuantity;
		this.totalCost = totalCost;
		this.totalCostOffers = totalCostOffers;
		this.orderItems = orderItems;
		this.order = order;
	}

	public void addItem(OrderItem item) {
		orderItems.add(item);
		item.setCart(this);
	}

	public void removeItem(OrderItem item) {
		orderItems.remove(item);
		item.setCart(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getTotalCostOffers() {
		return totalCostOffers;
	}

	public void setTotalCostOffers(Double totalCostOffers) {
		this.totalCostOffers = totalCostOffers;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", totalQuantity=" + totalQuantity + ", totalCost=" + totalCost + ", totalCostOffers="
				+ totalCostOffers + "]";
	}
}
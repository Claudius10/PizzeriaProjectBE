package PizzaApp.api.entity.cart;
import java.util.ArrayList;
import java.util.List;

import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "total_quantity")
	private int totalQuantity;

	@Column(name = "total_cost")
	private Double totalCost;

	@Column(name = "total_cost_offers")
	private Double totalCostOffers;

	// Cart PK is referenced by OrderItem FK
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "cart_id")
	private List<OrderItem> orderItems = new ArrayList<>();

	public Cart() {
	}

	public Cart(Long id, List<OrderItem> orderItems, int totalQuantity, double totalCost, double totalCostOffers) {
		this.id = id;
		this.orderItems = orderItems;
		this.totalQuantity = totalQuantity;
		this.totalCost = totalCost;
		this.totalCostOffers = totalCostOffers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
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

	public void setTotalCostOffers(double totalCostOffers) {
		this.totalCostOffers = totalCostOffers;
	}
}

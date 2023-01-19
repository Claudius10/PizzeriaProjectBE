package PizzaApp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "OrderItem")
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "format")
	private String format;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private double price;

	// FK in order_items references order_id PK
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	public OrderItem() {
	}

	public OrderItem(Long id, String name, String format, int quantity, double price, Order order) {
		this.id = id;
		this.name = name;
		this.format = format;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof OrderItem)) {
			return false;
		}

		return id != null && id.equals(((OrderItem) o).getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

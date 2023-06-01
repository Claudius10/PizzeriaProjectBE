package PizzaApp.api.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import PizzaApp.api.entity.cart.Cart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "product_type")
	private String productType;

	@Column(name = "name")
	private String name;

	@Column(name = "format")
	private String format;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private double price;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	public OrderItem() {
	}

	public OrderItem(Long id, String productType, String name, String format, int quantity, double price, Cart cart) {
		this.id = id;
		this.productType = productType;
		this.name = name;
		this.format = format;
		this.quantity = quantity;
		this.price = price;
		this.cart = cart;
	}

	public OrderItem(String productType, String name, String format, int quantity, double price, Cart cart) {
		this.productType = productType;
		this.name = name;
		this.format = format;
		this.quantity = quantity;
		this.price = price;
		this.cart = cart;
	}

	public OrderItem(String productType, String name, String format, int quantity, double price) {
		this.productType = productType;
		this.name = name;
		this.format = format;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof OrderItem))
			return false;

		return id != null && id.equals(((OrderItem) obj).getId());
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", productType=" + productType + ", name=" + name + ", format=" + format
				+ ", quantity=" + quantity + ", price=" + price + "]";
	}
}

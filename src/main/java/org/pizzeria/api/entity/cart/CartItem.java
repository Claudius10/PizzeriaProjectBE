package org.pizzeria.api.entity.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.pizzeria.api.exceptions.constraints.annotation.DoubleLength;
import org.pizzeria.api.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.utils.globals.ValidationResponses;

import java.util.Objects;

@Entity(name = "CartItem")
@Table(name = "cart_items")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_generator")
	@SequenceGenerator(name = "cart_item_generator", sequenceName = "cart_item_seq", allocationSize = 1)
	private Long id;

	@Column
	@NotBlank(message = ValidationResponses.CART_ITEM_TYPE_MISSING)
	private String productType;

	@Column
	@NotBlank(message = ValidationResponses.CART_ITEM_NAME_MISSING)
	private String name;

	@Column
	@NotBlank(message = ValidationResponses.CART_ITEM_FORMAT_MISSING)
	private String format;

	@Column
	@IntegerLength(min = 1, max = 2, message = ValidationResponses.CART_ITEM_MAX_QUANTITY_ERROR)
	private Integer quantity;

	@Column
	@DoubleLength(min = 1, max = 5, message = ValidationResponses.CART_ITEM_MAX_PRICE)
	private Double price;

	@Column
	private String image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Cart cart;

	public CartItem() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	private CartItem(Builder builder) {
		this.id = builder.id;
		this.productType = builder.productType;
		this.name = builder.name;
		this.format = builder.format;
		this.quantity = builder.quantity;
		this.price = builder.price;
		this.cart = null;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getImage() {
		return image;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof CartItem))
			return false;

		return id != null && id.equals(((CartItem) obj).getId());
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", productType=" + productType + ", name=" + name + ", format=" + format
				+ ", quantity=" + quantity + ", price=" + price + "]";
	}

	public boolean contentEquals(Object o) {
		CartItem cartItem = (CartItem) o;
		return Objects.equals(productType, cartItem.productType)
				&& Objects.equals(name, cartItem.name)
				&& Objects.equals(format, cartItem.format)
				&& Objects.equals(quantity, cartItem.quantity)
				&& Objects.equals(price, cartItem.price);
	}

	public static class Builder {
		private Long id;
		private String productType;
		private String name;
		private String format;
		private Integer quantity;
		private Double price;

		public Builder() {
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withProductType(String productType) {
			this.productType = productType;
			return this;
		}

		public Builder withWithName(String name) {
			this.name = name;
			return this;
		}

		public Builder withFormat(String format) {
			this.format = format;
			return this;
		}

		public Builder withQuantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public Builder withPrice(Double price) {
			this.price = price;
			return this;
		}

		public CartItem build() {
			return new CartItem(this);
		}
	}
}

package org.pizzeria.api.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.pizzeria.api.exceptions.constraints.annotation.DoubleLength;
import org.pizzeria.api.exceptions.constraints.annotation.DoubleLengthNullable;
import org.pizzeria.api.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.utils.globals.ValidationResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Cart")
@Table(name = "cart")
public class Cart {

	@Id
	private Long id;

	@Column
	@IntegerLength(min = 1, max = 2, message = ValidationResponses.CART_MAX_PRODUCTS_QUANTITY_ERROR)
	private Integer totalQuantity;

	@Column
	@DoubleLength(min = 1, max = 6, message = ValidationResponses.CART_COST_INVALID)
	private Double totalCost;

	@Column
	@DoubleLengthNullable(min = 0, max = 6, message = ValidationResponses.CART_COST_INVALID)
	private Double totalCostOffers;

	// INFO to remember about the Cart/OrderItem association:
	// given that Order & Cart association has CascadeType.ALL
	// and Cart & OrderItem bidirectional association also has CascadeType.ALL
	// when updating Cart, the merge operation is going to be cascaded to the
	// OrderItem association as well, so there's no need to manually
	// sync the bidirectional association
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<OrderItem> orderItems;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonBackReference
	private Order order;

	public Cart() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	public static class Builder {

		private final Cart cart;

		public Builder() {
			this.cart = new Cart();
		}

		public Builder withId(Long id) {
			cart.id = id;
			return this;
		}

		public Builder withTotalQuantity(Integer totalQuantity) {
			cart.totalQuantity = totalQuantity;
			return this;
		}

		public Builder withTotalCost(Double totalCost) {
			cart.totalCost = totalCost;
			return this;
		}

		public Builder withTotalCostOffers(Double totalCostOffers) {
			cart.totalCostOffers = totalCostOffers;
			return this;
		}

		public Builder withOrderItems(List<OrderItem> orderItems) {
			cart.orderItems = new ArrayList<>();
			for (OrderItem item : orderItems) {
				item.setId(null);
				cart.addItem(item);
			}
			return this;
		}

		public Builder withEmptyItemList() {
			cart.orderItems = new ArrayList<>();
			cart.totalQuantity = 0;
			return this;
		}

		public Cart build() {
			return cart;
		}
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

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
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

	public boolean contentEquals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cart cart = (Cart) o;

		// orderItem contentEquals
		List<Boolean> itemEqualityCheck = new ArrayList<>();
		for (int i = 0; i < orderItems.size(); i++) {
			for (int j = 0; j < cart.getOrderItems().size(); j++) {

				if (orderItems.get(i).contentEquals(cart.orderItems.get(j))) {
					itemEqualityCheck.add(true);

					// avoid i value becoming greater than orderItems.size() value
					if (i < orderItems.size() - 1) {
						// move to next i if i0 is equal to j0
						// to avoid comparing i0 to j1
						i++;
					}
				} else {
					itemEqualityCheck.add(false);
				}
			}
		}

		boolean areItemsEqual = true;
		for (Boolean bool : itemEqualityCheck) {
			if (!bool) {
				areItemsEqual = false;
				break;
			}
		}

		return Objects.equals(totalQuantity, cart.totalQuantity) &&
				Objects.equals(totalCost, cart.totalCost) &&
				Objects.equals(totalCostOffers, cart.totalCostOffers) &&
				areItemsEqual;
	}
}
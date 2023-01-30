package PizzaApp.api.entity;

import java.util.List;

public class OrderItems {
	
	private List<OrderItem> orderItems;

	public OrderItems () {}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	};
	
	
}

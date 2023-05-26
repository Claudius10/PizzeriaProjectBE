package PizzaApp.api.utility.order;

import PizzaApp.api.entity.order.Order;

public interface OrderUtility {

	public boolean isChangeRequestedValid(Order order);

	public boolean IsContactNumberValid(Order order);

	public boolean isCartEmpty(Order order);

	public void calculatePaymentChange(Order order);

	public void validate(Order order);
}

package PizzaApp.api.utility.order.interfaces;

import PizzaApp.api.entity.order.Order;

public interface OrderUtility {

	public void isRequestWithinWorkingHours();
	
	public void validate(Order order);

	public boolean isChangeRequestedValid(Order order);

	public boolean IsContactNumberValid(Order order);

	public boolean isCartValid(Order order);

	public void calculatePaymentChange(Order order);
}

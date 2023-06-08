package PizzaApp.api.utility.order.interfaces;

import PizzaApp.api.entity.order.Order;

public interface OrderUtility {

	void isRequestWithinWorkingHours();

	void validate(Order order);

	boolean isChangeRequestedValid(Order order);

	boolean IsContactNumberValid(Order order);

	boolean isCartValid(Order order);

	void calculatePaymentChange(Order order);
}

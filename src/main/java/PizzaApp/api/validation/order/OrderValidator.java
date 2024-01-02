package PizzaApp.api.validation.order;

import PizzaApp.api.entity.order.Order;

import java.time.LocalDateTime;

public interface OrderValidator {

	String validate(Order order);

	String isChangeRequestedValid(Order order);

	String isCartValid(Order order);

	void calculatePaymentChange(Order order);

	String isRequestWithinWorkingHours();

	String validateUpdate(Order order);

	void isCartUpdateTimeLimitValid(Order order);

	String isOrderDataUpdateTimeLimitValid();

	String isOrderDeleteTimeLimitValid(LocalDateTime now);
}

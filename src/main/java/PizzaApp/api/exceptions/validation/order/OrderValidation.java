package PizzaApp.api.exceptions.validation.order;

import PizzaApp.api.entity.order.Order;

import java.time.LocalDateTime;

public interface OrderValidation {

	OrderValidator setCurrentTime();

	void validate(Order order);

	void isChangeRequestedValid(Order order);

	void IsContactNumberValid(Order order);

	void isEmailValid(Order order);

	void isCartValid(Order order);

	void calculatePaymentChange(Order order);

	void isRequestWithinWorkingHours();

	void validateMutation(Order order);

	void isCartUpdateTimeLimitValid(Order order);

	void isOrderDataUpdateTimeLimitValid();

	void isOrderDeleteTimeLimitValid(LocalDateTime now);
}
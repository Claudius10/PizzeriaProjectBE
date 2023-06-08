package PizzaApp.api.utility.order.interfaces;

import java.time.LocalDateTime;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.utility.order.OrderUpdateUtility;

public interface OrderUpdateTimeLimits {

	OrderUpdateUtility init(LocalDateTime createdOn);

	void validate(Order order);

	void isCartUpdateTimeLimitValid(Order order);

	void isOrderDataUpdateTimeLimitValid();

	void isOrderDeleteTimeLimitValid();
}
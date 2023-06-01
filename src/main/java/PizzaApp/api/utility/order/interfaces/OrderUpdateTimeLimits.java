package PizzaApp.api.utility.order.interfaces;

import java.time.LocalDateTime;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.utility.order.OrderUpdateUtility;

public interface OrderUpdateTimeLimits {

	public OrderUpdateUtility init(LocalDateTime createdOn);

	public void validate(Order order);

	public void isCartUpdateTimeLimitValid(Order order);

	public void isOrderDataUpdateTimeLimitValid();

	public void isOrderDeleteTimeLimitValid();
}
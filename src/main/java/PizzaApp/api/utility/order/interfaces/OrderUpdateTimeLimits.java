package PizzaApp.api.utility.order.interfaces;

import java.time.LocalDateTime;
import PizzaApp.api.entity.order.Order;

public interface OrderUpdateTimeLimits {

	public void validate(Order order);

	public void isCartUpdateTimeLimitValid(Order order);

	public void isOrderDataUpdateTimeLimitValid();

	public void isOrderDeleteTimeLimitValid(LocalDateTime createdOn);

}
package PizzaApp.api.utility.order.interfaces;

import java.time.LocalDateTime;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.utility.order.OrderUpdateUtility;

public interface OrderUpdateTimeLimits {

	public OrderUpdateUtility get(LocalDateTime createdOn, Cart cart);

	public OrderUpdateUtility get(LocalDateTime createdOn);

	public void validate();

	public void validateDelete();

	public boolean isCartUpdateTimeLimitValid();

	public boolean isOrderDataUpdateTimeLimitValid();

	public boolean isOrderDeleteTimeLimitValid();
}
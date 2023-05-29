package PizzaApp.api.utility.order.interfaces;

import java.time.LocalDateTime;
import PizzaApp.api.entity.cart.Cart;

public interface OrderUpdateTimeLimits {

	public OrderUpdateTimeLimits get(LocalDateTime createdOn, Cart cart);

	public OrderUpdateTimeLimits get(LocalDateTime createdOn);

	public void validate();

	public void validateDelete();

	public boolean isCartUpdateTimeLimitValid();

	public boolean isOrderDataUpdateTimeLimitValid();

	public boolean isOrderDeleteTimeLimitValid();
}
package PizzaApp.api.validation.order;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.Order;

import java.time.LocalDateTime;

public interface OrderValidator {

	OrderValidationResult validate(Order order);

	OrderValidationResult validateUpdate(Order order);

	OrderValidationResult validateDelete(Long orderId);

	boolean isCartEmpty(Cart cart);

	boolean isProductListSizeValid(Cart cart);

	boolean isProductsQuantityValid(Cart cart);

	boolean isChangeRequestedValid(Double changeRequested, Cart cart);

	boolean isCartUpdateTimeLimitValid(LocalDateTime createdOn);

	boolean isOrderDataUpdateTimeLimitValid(LocalDateTime createdOn);

	void calculatePaymentChange(Order order);

	boolean isRequestWithinWorkingHours();
}

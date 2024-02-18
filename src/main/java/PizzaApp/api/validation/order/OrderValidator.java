package PizzaApp.api.validation.order;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

public interface OrderValidator {

	OrderValidationResult validate(Cart cart, OrderDetails orderDetails);

	OrderValidationResult validateUpdate(Cart cart, OrderDetails orderDetails, LocalDateTime createdOn);

	OrderValidationResult validateDelete(Long orderId);

	boolean isCartEmpty(Cart cart);

	boolean isProductListSizeValid(Cart cart);

	boolean isProductsQuantityValid(Cart cart);

	boolean isChangeRequestedValid(Double changeRequested, Cart cart);

	boolean isCartUpdateTimeLimitValid(LocalDateTime createdOn);

	boolean isOrderDataUpdateTimeLimitValid(LocalDateTime createdOn);

	void calculatePaymentChange(Cart cart, OrderDetails orderDetails);

	boolean isRequestWithinWorkingHours();
}

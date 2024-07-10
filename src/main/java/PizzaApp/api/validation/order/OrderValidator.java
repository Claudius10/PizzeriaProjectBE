package PizzaApp.api.validation.order;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderValidator {

	OrderValidationResult validate(Cart cart, OrderDetails orderDetails);

	OrderValidationResult validateUpdate(Cart cart, OrderDetails orderDetails, LocalDateTime createdOn);

	OrderValidationResult validateDelete(Long orderId);

	boolean isCartEmpty(Cart cart);

	boolean isChangeRequestedValid(Double changeRequested, Double totalCostAfterOffers, Double totalCost);

	boolean isCartUpdateTimeLimitValid(LocalDateTime createdOn);

	boolean isOrderDataUpdateTimeLimitValid(LocalDateTime createdOn);

	Double calculatePaymentChange(Double changeRequested, Double totalCost, Double totalCostAfterOffers);
}

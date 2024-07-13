package org.pizzeria.api.validation.order;

import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.OrderDetails;

import java.time.LocalDateTime;

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

package PizzaApp.api.validation.order;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utils.globals.ValidationResponses;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderValidatorImpl implements OrderValidator {

	private final OrderService orderService;

	public OrderValidatorImpl(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public OrderValidationResult validate(Cart cart, OrderDetails orderDetails) {
		if (isCartEmpty(cart)) {
			return new OrderValidationResult(ValidationResponses.CART_IS_EMPTY);
		}

		if (!isChangeRequestedValid(orderDetails.getChangeRequested(), cart.getTotalCostOffers(), cart.getTotalCost())) {
			return new OrderValidationResult(ValidationResponses.ORDER_DETAILS_CHANGE_REQUESTED);
		}

		orderDetails.setPaymentChange(calculatePaymentChange(orderDetails.getChangeRequested(), cart.getTotalCost(), cart.getTotalCostOffers()));
		return new OrderValidationResult();
	}

	@Override
	public OrderValidationResult validateUpdate(Cart cart, OrderDetails orderDetails, LocalDateTime createdOn) {
		if (!isOrderDataUpdateTimeLimitValid(createdOn)) {
			return new OrderValidationResult(ValidationResponses.ORDER_UPDATE_TIME_ERROR);
		}

		if (!isCartUpdateTimeLimitValid(createdOn)) {
			OrderValidationResult result = validate(cart, orderDetails);
			if (result.isValid()) {
				return new OrderValidationResult(false);
			}
		}

		return validate(cart, orderDetails);
	}

	public OrderValidationResult validateDelete(Long orderId) {
		LocalDateTime createdOn = orderService.findCreatedOnById(orderId);

		if (createdOn == null) {
			return new OrderValidationResult(String.format(ValidationResponses.ORDER_NOT_FOUND, orderId));
		}

		if (LocalDateTime.now().isAfter(createdOn.plusMinutes(20))) {
			return new OrderValidationResult(ValidationResponses.ORDER_DELETE_TIME_ERROR);
		}

		return new OrderValidationResult();
	}

	@Override
	public boolean isCartEmpty(Cart cart) {
		return cart != null && cart.getOrderItems().isEmpty() && cart.getTotalQuantity() == 0;
	}

	// changeRequested > totalCostAfterOffers || changeRequested > totalCost
	@Override
	public boolean isChangeRequestedValid(Double changeRequested, Double totalCostAfterOffers, Double totalCost) {
		if (changeRequested == null) {
			return true;
		}
		return (totalCostAfterOffers <= 0 || changeRequested >= totalCostAfterOffers) && (totalCostAfterOffers != 0 || changeRequested >= totalCost);
	}

	@Override
	public boolean isCartUpdateTimeLimitValid(LocalDateTime createdOn) {
		return LocalDateTime.now().isBefore(createdOn.plusMinutes(10));
	}

	@Override
	public boolean isOrderDataUpdateTimeLimitValid(LocalDateTime createdOn) {
		return LocalDateTime.now().isBefore(createdOn.plusMinutes(15));
	}

	// changeRequested == null || (changeRequested - totalCostOffers || totalCost)
	@Override
	public Double calculatePaymentChange(Double changeRequested, Double totalCost, Double totalCostAfterOffers) {
		if (changeRequested == null) {
			return null;
		}

		if (totalCostAfterOffers > 0) {
			return (double) Math.round((changeRequested - totalCostAfterOffers) * 100) / 100;
		}

		return (double) Math.round((changeRequested - totalCost) * 100) / 100;
	}
}
package PizzaApp.api.utility.order;

import org.springframework.stereotype.Component;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.exceptions.ChangeRequestedNotValidException;
import PizzaApp.api.exceptions.EmptyCartException;

//utility methods for OrderRepository
@Component
public class OrderUtilityImpl implements OrderUtility {

	public OrderUtilityImpl() {
	}

	// value of requested change has to be greater than
	// totalCost or totalOfferCost
	@Override
	public boolean isChangeRequestedValid(Order order) {
		// if change requested == null, then it goes into DB as 0
		// so OK
		if (order.getOrderDetails().getChangeRequested() == null) {
			return true;

		} else if (order.getCart().getTotalCostOffers() > 0
				&& order.getOrderDetails().getChangeRequested() > order.getCart().getTotalCostOffers()) {
			return true;

		} else if (order.getCart().getTotalCostOffers() == 0
				&& order.getOrderDetails().getChangeRequested() > order.getCart().getTotalCost()) {
			return true;

		} else {
			// here got to throw custom error and handle it in GlobalExceptionHandler
			throw new ChangeRequestedNotValidException(
					"El valor del cambio de efectivo solicitado no puede ser menor o igual "
							+ "que el total/total con ofertas.");
		}
	}

	@Override
	public boolean isCartEmpty(Order order) {
		if (order.getCart() != null && !order.getCart().getOrderItems().isEmpty()
				&& order.getCart().getTotalQuantity() > 0) {
			return true;
		} else {
			throw new EmptyCartException("La cesta no puede ser vacía.");
		}
	}

	// calculate totalCost or totalCostOffers - changeRequested
	// the result being the change to give back to the client
	@Override
	public Double calculatePaymentChange(Order order) {
		// check whatever user introduced any change request
		if (order.getOrderDetails().getChangeRequested() != null) {

			// if yes and there is a totalCostOffers
			if (order.getCart().getTotalCostOffers() > 0) {
				// return the calculation
				return order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCostOffers();
			} else {
				// if yes and there is no totalCostOffers, just totalCost
				// return the calculation
				return order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCost();
			}
		} else {
			// if user did not introduce any change request
			// return for the db
			return 0.0;
		}
	}
}

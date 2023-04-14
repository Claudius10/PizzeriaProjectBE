package PizzaApp.api.utility.order;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.exceptions.ChangeRequestedNotValidException;

//utility methods for OrderRepository
public class OrderUtilityMethods {

	public OrderUtilityMethods() {
	}

	// value of requested change has to be greater than
	// totalCost or totalOfferCost
	// same as in front-end
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
							+ "que el total/total con ofertas");
		}
	}

	// calculate totalCost or totalCostOffers - changeRequested
	// being the change to give back to client
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
			// return 0 for the db
			return 0.0;
		}
	}

}

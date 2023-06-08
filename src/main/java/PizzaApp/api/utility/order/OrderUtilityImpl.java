package PizzaApp.api.utility.order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.utility.order.interfaces.OrderUtility;
import PizzaApp.api.validation.exceptions.EmptyCartException;
import PizzaApp.api.validation.exceptions.InvalidChangeRequestedException;
import PizzaApp.api.validation.exceptions.InvalidContactTelephoneException;
import PizzaApp.api.validation.exceptions.StoreNotOpenException;

@Component
public class OrderUtilityImpl implements OrderUtility {

	private final Logger logger = Logger.getLogger(getClass().getName());

	public OrderUtilityImpl() {

	}

	@Override
	public void validate(Order order) {
		isRequestWithinWorkingHours();
		IsContactNumberValid(order);
		isCartValid(order);
		isChangeRequestedValid(order);
		calculatePaymentChange(order);
	}

	@Override
	public void isRequestWithinWorkingHours() {

		// getting now plus 2 hours since host JVM is UTC +00:00
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).plusHours(2);
		Instant nowInstant = now.toInstant(ZoneOffset.UTC);
		Date date = Date.from(nowInstant);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		logger.info(String.format("Current hour: %s ", hour));
		logger.info(String.format("Current minutes: %s ", minutes));

		if (hour < 12 || hour == 23 && minutes > 40) {
			throw new StoreNotOpenException("El horario es de las 12:00h hasta las 23:40 horas.");
		}
	}

	@Override
	public boolean IsContactNumberValid(Order order) {
		// validate making sure its size is min 9 and max 9
		// if it's not, throw exception
		if (order.getContactTel() != null && String.valueOf(order.getContactTel().intValue()).length() == 9) {
			return true;
		} else {
			throw new InvalidContactTelephoneException("Teléfono: mín 9 digitos, máx 9 digitos");
		}
	}

	@Override
	public boolean isCartValid(Order order) {
		if (order.getCart() != null && !order.getCart().getOrderItems().isEmpty()
				&& order.getCart().getTotalQuantity() > 0) {
			return true;
		} else {
			throw new EmptyCartException("La cesta no puede ser vacía.");
		}
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
			throw new InvalidChangeRequestedException(
					"El valor del cambio de efectivo solicitado no puede ser menor o igual "
							+ "que el total/total con ofertas.");
		}
	}

	// calculate totalCost or totalCostOffers - changeRequested
	// the result being the change to give back to the client
	@Override
	public void calculatePaymentChange(Order order) {
		// check whatever user introduced any change request
		if (order.getOrderDetails().getChangeRequested() != null) {

			// if yes and there is a totalCostOffers
			if (order.getCart().getTotalCostOffers() > 0) {
				// set the calculation
				order.getOrderDetails().setPaymentChange(
						order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCostOffers());
			} else {
				// if yes and there is no totalCostOffers, just totalCost
				// set the corresponding calculation
				order.getOrderDetails().setPaymentChange(
						order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCost());
			}
		} else {
			// if user did not introduce any change request
			// return for the db
			order.getOrderDetails().setPaymentChange(null);
		}
	}
}
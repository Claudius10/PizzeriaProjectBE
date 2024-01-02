package PizzaApp.api.validation.order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import PizzaApp.api.entity.order.OrderItem;
import org.springframework.stereotype.Component;
import PizzaApp.api.entity.order.Order;

@Component
public class OrderValidatorImpl implements OrderValidator {

	private LocalDateTime now, createdOn;

	@Override
	public String validateUpdate(Order order) {
		validate(order);
		setNow(LocalDateTime.now());
		setCreatedOn(order.getCreatedOn());
		isCartUpdateTimeLimitValid(order); // off for dev (unless testing)
		return isOrderDataUpdateTimeLimitValid();
	}

	@Override
	public String validate(Order order) {
/*		String isStoreOpen = isRequestWithinWorkingHours(); // off for dev
		if (isStoreOpen != null) {
			return isStoreOpen;
		}*/

		String isCartValid = isCartValid(order);
		if (isCartValid != null) {
			return isCartValid;
		}

		String isChangeRequestValid = isChangeRequestedValid(order);
		if (isChangeRequestValid != null) {
			return isChangeRequestValid;
		}

		calculatePaymentChange(order);
		return null;
	}


	@Override
	public void isCartUpdateTimeLimitValid(Order order) {
		LocalDateTime cartUpdateTimeLimit = createdOn.plusMinutes(10);
		if (now.isAfter(cartUpdateTimeLimit)) {
			order.setCart(null);
		}
	}

	@Override
	public String isOrderDataUpdateTimeLimitValid() {
		LocalDateTime orderDataUpdateTimeLimit = createdOn.plusMinutes(15);
		if (now.isAfter(orderDataUpdateTimeLimit)) {
			return "El tiempo límite para actualizar el pedido (15 minutos) ha finalizado";
		}
		return null;
	}

	@Override
	public String isOrderDeleteTimeLimitValid(LocalDateTime createdOn) {
		setNow(LocalDateTime.now());
		LocalDateTime orderDeleteTimeLimit = createdOn.plusMinutes(20);
		if (now.isAfter(orderDeleteTimeLimit)) {
			return "El tiempo límite para anular el pedido (20 minutos) ha finalizado";
		}
		return null;
	}

	@Override
	public String isRequestWithinWorkingHours() {
		// getting now plus 2 hours since host JVM is UTC +00:00
		LocalDateTime localNow = LocalDateTime.now(ZoneOffset.UTC).plusHours(2);
		Instant nowInstant = localNow.toInstant(ZoneOffset.UTC);
		Date date = Date.from(nowInstant);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		if (hour < 12 || hour == 23 && minutes > 40) {
			return "La tienda está cerrada. El horario es de las 12:00h hasta las 23:40 horas.";
		}
		return null;
	}

	@Override
	public String isCartValid(Order order) {
		if (order.getCart() == null || order.getCart().getOrderItems().isEmpty() || order.getCart().getTotalQuantity() <= 0) {
			return "La cesta no puede ser vacía";
		}

		if (order.getCart().getOrderItems().size() > 20) {
			return "Se ha superado el límite de artículos por pedido (20 art.). Contacte con nosotros" +
					" si desea realizar el pedido";
		}

		for (OrderItem item : order.getCart().getOrderItems()) {
			if (item.getQuantity() > 20) {
				return "Se ha superado la cantidad máxima por artículo (20 uds.). Contacte con " +
						"nosotros si desea realizar el pedido";
			}
		}
		return null;
	}

	// value of requested change has to be greater than
	// totalCost or totalOfferCost
	@Override
	public String isChangeRequestedValid(Order order) {
		if (order.getOrderDetails().getChangeRequested() != null) {

			if ((order.getCart().getTotalCostOffers() > 0 &&

					order.getOrderDetails().getChangeRequested() < order.getCart().getTotalCostOffers()) ||

					(order.getCart().getTotalCostOffers() == 0 &&

							order.getOrderDetails().getChangeRequested() < order.getCart().getTotalCost())) {

				return "El valor del cambio de efectivo solicitado no puede ser menor o igual "
						+ "que el total/total con ofertas.";
			}
		}
		return null;
	}

	// calculate totalCost or totalCostOffers - changeRequested
	// the result being the change to give back to the client
	@Override
	public void calculatePaymentChange(Order order) {
		if (order.getOrderDetails().getChangeRequested() == null) {
			order.getOrderDetails().setPaymentChange(null);
		} else {

			if (order.getCart().getTotalCostOffers() > 0) {
				order.getOrderDetails().setPaymentChange
						(order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCostOffers());

			} else {
				order.getOrderDetails().setPaymentChange
						(order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCost());
			}
		}
	}

	public void setNow(LocalDateTime now) {
		this.now = now;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
}
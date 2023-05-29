package PizzaApp.api.utility.order;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.utility.order.interfaces.OrderUpdateTimeLimits;
import PizzaApp.api.validation.exceptions.CartUpdateTimeLimitException;
import PizzaApp.api.validation.exceptions.OrderDataUpdateTimeLimitException;
import PizzaApp.api.validation.exceptions.OrderDeleteTimeLimitException;

@Component
public class OrderUpdateUtility implements OrderUpdateTimeLimits {

	private final Logger logger = Logger.getLogger(getClass().getName());
	private final LocalDateTime now = LocalDateTime.now();
	private LocalDateTime createdOn;
	private Cart cart;
	private Long hoursDiff;
	private Long minutesDiff;

	public OrderUpdateUtility() {
	}

	@Override
	public OrderUpdateTimeLimits get(LocalDateTime orderCreatedOn) {
		setCreatedOn(orderCreatedOn);
		setHoursDiff(ChronoUnit.HOURS.between(orderCreatedOn, now));
		setMinutesDiff(ChronoUnit.MINUTES.between(orderCreatedOn, now));
		setCart(null);
		return this;
	}

	@Override
	public OrderUpdateUtility get(LocalDateTime orderCreatedOn, Cart cart) {
		setCreatedOn(orderCreatedOn);
		setHoursDiff(ChronoUnit.HOURS.between(orderCreatedOn, now));
		setMinutesDiff(ChronoUnit.MINUTES.between(orderCreatedOn, now));
		setCart(cart);
		return this;
	}

	@Override
	public void validate() {
		isCartUpdateTimeLimitValid();
		isOrderDataUpdateTimeLimitValid();
	}

	@Override
	public void validateDelete() {
		isOrderDeleteTimeLimitValid();
	}

	@Override
	public boolean isCartUpdateTimeLimitValid() {

		boolean updateAllowed;

		LocalDateTime cartUpdateTimeLimit = createdOn.plusMinutes(15);

		if (now.isBefore(cartUpdateTimeLimit)) {

			updateAllowed = true;
			logger.info(String.format("Cart update is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
					updateAllowed, createdOn, now, hoursDiff, minutesDiff));
			return true;

		} else {
			// if cartUpdateTimeLimit passed but cart is not being updated
			// return true to not throw
			if (cart == null) {
				return true;
			} else {
				updateAllowed = false;
				logger.info(
						String.format("Cart update is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
								updateAllowed, createdOn, now, hoursDiff, minutesDiff));
				throw new CartUpdateTimeLimitException(
						"El tiempo limite para actualizar la cesta (15 minutos) ha finalizado");
			}
		}
	}

	@Override
	public boolean isOrderDataUpdateTimeLimitValid() {

		boolean updateAllowed;

		LocalDateTime orderDataUpdateTimeLimit = createdOn.plusMinutes(20);

		if (now.isBefore(orderDataUpdateTimeLimit)) {

			updateAllowed = true;
			logger.info(String.format(
					"Order data update is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
					updateAllowed, createdOn, now, hoursDiff, minutesDiff));
			return true;

		} else {

			updateAllowed = false;
			logger.info(String.format(
					"Order data update is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
					updateAllowed, createdOn, now, hoursDiff, minutesDiff));

			throw new OrderDataUpdateTimeLimitException(
					"El tiempo limite para actualizar los datos del pedido (20 minutos) ha finalizado");
		}
	}

	@Override
	public boolean isOrderDeleteTimeLimitValid() {

		boolean updateAllowed;

		LocalDateTime orderDeleteTimeLimit = createdOn.plusMinutes(35);

		if (now.isBefore(orderDeleteTimeLimit)) {

			updateAllowed = true;
			logger.info(
					String.format("Order delete is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
							updateAllowed, createdOn, now, hoursDiff, minutesDiff));
			return true;

		} else {

			updateAllowed = false;
			logger.info(
					String.format("Order delete is allowed: %s (createdOn: %s | now: %s | difference: %s h : %s m) ",
							updateAllowed, createdOn, now, hoursDiff, minutesDiff));
			throw new OrderDeleteTimeLimitException(
					"El tiempo limite para anular el pedido (35 minutos) ha finalizado");
		}
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Long getHoursDiff() {
		return hoursDiff;
	}

	public void setHoursDiff(Long hoursDiff) {
		this.hoursDiff = hoursDiff;
	}

	public Long getMinutesDiff() {
		return minutesDiff;
	}

	public void setMinutesDiff(Long minutesDiff) {
		this.minutesDiff = minutesDiff;
	}
}
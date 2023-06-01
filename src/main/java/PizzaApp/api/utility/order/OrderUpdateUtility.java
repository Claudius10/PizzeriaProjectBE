package PizzaApp.api.utility.order;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.utility.order.interfaces.OrderUpdateTimeLimits;
import PizzaApp.api.validation.exceptions.OrderDataUpdateTimeLimitException;
import PizzaApp.api.validation.exceptions.OrderDeleteTimeLimitException;

@Component
public class OrderUpdateUtility implements OrderUpdateTimeLimits {

	private final Logger logger = Logger.getLogger(getClass().getName());
	private LocalDateTime now;
	private LocalDateTime createdOn;
	private Long hoursDiff;
	private Long minutesDiff;

	public OrderUpdateUtility() {
	}

	@Override
	public OrderUpdateUtility init(LocalDateTime createdOn) {
		setNow(LocalDateTime.now());
		setCreatedOn(createdOn);
		setHoursDiff(ChronoUnit.HOURS.between(createdOn, now));
		setMinutesDiff(ChronoUnit.MINUTES.between(createdOn, now));
		return this;
	}

	@Override
	public void validate(Order order) {
		isCartUpdateTimeLimitValid(order);
		isOrderDataUpdateTimeLimitValid();
	}

	@Override
	public void isCartUpdateTimeLimitValid(Order order) {
		LocalDateTime cartUpdateTimeLimit = createdOn.plusMinutes(15);
		if (now.isAfter(cartUpdateTimeLimit)) {
			order.setCart(null);
		}
	}

	@Override
	public void isOrderDataUpdateTimeLimitValid() {
		LocalDateTime orderDataUpdateTimeLimit = createdOn.plusMinutes(20);
		if (now.isAfter(orderDataUpdateTimeLimit)) {
			logger.info(String.format(
					"Order data update is not allowed (createdOn: %s | now: %s | difference: %s h : %s m) ", createdOn,
					now, hoursDiff, minutesDiff));
			throw new OrderDataUpdateTimeLimitException(
					"El tiempo límite para actualizar los datos del pedido (20 minutos) ha finalizado");
		}
	}

	@Override
	public void isOrderDeleteTimeLimitValid() {
		LocalDateTime orderDeleteTimeLimit = createdOn.plusMinutes(35);
		if (now.isAfter(orderDeleteTimeLimit)) {
			logger.info(
					String.format("Order delete is not allowed (createdOn: %s | now: %s | difference: %s h : %s m) ",
							createdOn, now, hoursDiff, minutesDiff));
			throw new OrderDeleteTimeLimitException(
					"El tiempo límite para anular el pedido (35 minutos) ha finalizado");
		}
	}

	public LocalDateTime getNow() {
		return now;
	}

	public void setNow(LocalDateTime now) {
		this.now = now;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
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
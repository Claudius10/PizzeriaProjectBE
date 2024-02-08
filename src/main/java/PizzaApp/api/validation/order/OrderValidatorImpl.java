package PizzaApp.api.validation.order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.repos.order.OrderRepository;
import org.springframework.stereotype.Component;
import PizzaApp.api.entity.order.Order;

@Component
public class OrderValidatorImpl implements OrderValidator {

	private final OrderRepository orderRepository;

	public OrderValidatorImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public OrderValidationResult validate(Order order) {
		if (!isCartEmpty(order.getCart())) {
			return new OrderValidationResult("La cesta no puede ser vacía.");
		}

		if (!isProductListSizeValid(order.getCart())) {
			return new OrderValidationResult("Se ha superado el límite de artículos por pedido (20). Contacte con " + "nosotros si desea realizar el pedido.");
		}

		if (!isProductsQuantityValid(order.getCart())) {
			return new OrderValidationResult("Se ha superado el límite de unidades por artículo (20). Contacte con " + "nosotros si desea realizar el pedido.");
		}

		if (!isChangeRequestedValid(order.getOrderDetails().getChangeRequested(), order.getCart())) {
			return new OrderValidationResult("El valor del cambio de efectivo solicitado no puede ser menor o igual " + "que el total o total con ofertas.");
		}

		// NOTE - turn on for prod
		if (!isRequestWithinWorkingHours()) {
			return new OrderValidationResult("La tienda está cerrada. El horario es de las 12:00h hasta las 23:40 horas.");
		}

		calculatePaymentChange(order);
		return new OrderValidationResult();
	}

	@Override
	public OrderValidationResult validateUpdate(Order order) {
		if (!isOrderDataUpdateTimeLimitValid(order.getCreatedOn())) {
			return new OrderValidationResult("El tiempo límite para actualizar el pedido (15 minutos) ha finalizado.");
		}

		if (!isCartUpdateTimeLimitValid(order.getCreatedOn())) {
			OrderValidationResult result = validate(order);
			if (result.isValid()) {
				return new OrderValidationResult(false);
			}
		}

		return validate(order);
	}

	public OrderValidationResult validateDelete(Long orderId) {
		if (LocalDateTime.now().isAfter(orderRepository.findCreatedOnById(orderId).createdOn().plusMinutes(20))) {
			return new OrderValidationResult("El tiempo límite para anular el pedido (20 minutos) ha finalizado.");
		}
		return new OrderValidationResult();
	}

	@Override
	public boolean isCartEmpty(Cart cart) {
		return cart != null && !cart.getOrderItems().isEmpty() && cart.getTotalQuantity() > 0;
	}

	@Override
	public boolean isProductListSizeValid(Cart cart) {
		return cart.getOrderItems().size() <= 20;
	}

	@Override
	public boolean isProductsQuantityValid(Cart cart) {
		for (OrderItem item : cart.getOrderItems()) {
			if (item.getQuantity() > 20) {
				return false;
			}
		}
		return true;
	}

	// changeRequested > totalCost || totalOfferCost
	@Override
	public boolean isChangeRequestedValid(Double changeRequested, Cart cart) {
		if (changeRequested == null) {
			return true;
		}
		return (cart.getTotalCostOffers() <= 0 || changeRequested >= cart.getTotalCostOffers()) && (cart.getTotalCostOffers() != 0 || changeRequested >= cart.getTotalCost());
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
	public void calculatePaymentChange(Order order) {
		if (order.getOrderDetails().getChangeRequested() == null) {
			order.getOrderDetails().setPaymentChange(null);
			return;
		}

		if (order.getCart().getTotalCostOffers() != 0) {
			order.getOrderDetails().setPaymentChange(order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCostOffers());
			return;
		}

		if (order.getCart().getTotalCostOffers() == 0) {
			order.getOrderDetails().setPaymentChange(order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCost());
		}
	}

	@Override
	public boolean isRequestWithinWorkingHours() {
		// getting now plus 1 hour since host JVM is UTC +00:00
		LocalDateTime localNow = LocalDateTime.now(ZoneOffset.UTC).plusHours(1);
		Instant nowInstant = localNow.toInstant(ZoneOffset.UTC);
		Date date = Date.from(nowInstant);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		return hour >= 9 && (hour != 23 || minutes <= 30);
	}
}
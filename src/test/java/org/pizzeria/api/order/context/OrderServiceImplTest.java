package org.pizzeria.api.order.context;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.web.dto.order.dto.NewUserOrderDTO;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.repos.order.OrderRepository;
import org.pizzeria.api.services.address.AddressService;
import org.pizzeria.api.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class OrderServiceImplTest {

	private final OrderRepository orderRepository;

	private final AddressService addressService;

	private final UserService userService;

	public OrderServiceImplTest(
			OrderRepository orderRepository,
			AddressService addressService,
			UserService userService) {
		this.orderRepository = orderRepository;
		this.addressService = addressService;
		this.userService = userService;
	}

	public Long createOrderTestSubjects(NewUserOrderDTO newUserOrder, Long userId, LocalDateTime createdOn) {
		User user = userService.findUserReference(userId);
		Address address = addressService.findReference(newUserOrder.addressId());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newUserOrder.cart().getTotalQuantity())
				.withTotalCost(newUserOrder.cart().getTotalCost())
				.withTotalCostOffers(newUserOrder.cart().getTotalCostOffers())
				.withCartItems(newUserOrder.cart().getCartItems())
				.build();

		Order order = new Order.Builder()
				.withCreatedOn(createdOn)
				.withFormattedCreatedOn(createdOn.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - " +
						"dd/MM/yyyy")))
				.withUser(user)
				.withAddress(address)
				.withOrderDetails(newUserOrder.orderDetails())
				.withCart(cart)
				.build();

		return orderRepository.save(order).getId();
	}
}

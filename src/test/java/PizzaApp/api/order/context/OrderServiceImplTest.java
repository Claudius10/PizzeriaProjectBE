package PizzaApp.api.order.context;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.user.UserService;
import jakarta.transaction.Transactional;
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

	public Long createOrderTestSubjects(NewUserOrderDTO newUserOrder, LocalDateTime createdOn) {
		User user = userService.findReference(newUserOrder.userId());
		Address address = addressService.findReference(newUserOrder.addressId());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newUserOrder.cart().getTotalQuantity())
				.withTotalCost(newUserOrder.cart().getTotalCost())
				.withTotalCostOffers(newUserOrder.cart().getTotalCostOffers())
				.withOrderItems(newUserOrder.cart().getOrderItems())
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

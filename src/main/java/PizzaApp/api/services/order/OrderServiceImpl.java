package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import PizzaApp.api.entity.dto.order.*;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.services.user.account.UserDataService;
import PizzaApp.api.services.user.address.AddressService;
import org.springframework.stereotype.Service;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.validation.order.OrderValidator;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final AddressService addressService;

	private final UserDataService userDataService;

	private final OrderValidator validator;

	public OrderServiceImpl
			(OrderRepository orderRepository,
			 AddressService addressService,
			 UserDataService userDataService,
			 OrderValidator validator) {
		this.orderRepository = orderRepository;
		this.addressService = addressService;
		this.userDataService = userDataService;

		this.validator = validator;
	}

	@Override
	public Long createAnonOrder(Order order) {
		Optional<Address> dbAddress = addressService.find(order.getAddress());
		dbAddress.ifPresent(order::setAddress);
		order.setUserData(null); // anon order

		// set created on in standard UTC (+00:00)
		order.setCreatedOn(LocalDateTime.now());

		// set formatted created on in UTC +2 since FE is +02:00
		order.setFormattedCreatedOn(order.getCreatedOn().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

		return orderRepository.createOrder(order);
	}

	@Override
	public Long createUserOrder(UserOrderDTO userOrderDTO) {
		Address dbAddress = addressService.findReference(String.valueOf(userOrderDTO.userOrderData().addressId()));
		UserData dbUserData = userDataService.findReference(userOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withAddress(dbAddress)
				.withCustomerName(userOrderDTO.userOrderData().customerName())
				.withContactTel(userOrderDTO.userOrderData().tel())
				.withEmail(userOrderDTO.userOrderData().email())
				.withOrderDetails(userOrderDTO.orderDetails())
				.withCart(userOrderDTO.cart())
				.build();

		dbUserData.addOrder(order); // also does order.setUserData(this);

		// set created on in standard UTC (+00:00)
		order.setCreatedOn(LocalDateTime.now());

		// set formatted created on in UTC +2 since FE is +02:00
		order.setFormattedCreatedOn(order.getCreatedOn().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

		// validate order
		validator.validate(order);

		return orderRepository.createOrder(order);
	}

	@Override
	public Long updateUserOrder(UserOrderDTO userOrderDTO) {
		Address dbAddress = addressService.findReference(String.valueOf(userOrderDTO.userOrderData().addressId()));
		UserData dbUserData = userDataService.findReference(userOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(userOrderDTO.createdOn())
				.withFormattedCreatedOn(userOrderDTO.formattedCreatedOn())
				.withUpdatedOn(LocalDateTime.now())
				.withFormattedUpdatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withId(userOrderDTO.orderId())
				.withUser(dbUserData)
				.withAddress(dbAddress)
				.withCustomerName(userOrderDTO.userOrderData().customerName())
				.withEmail(userOrderDTO.userOrderData().email())
				.withContactTel(userOrderDTO.userOrderData().tel())
				.build();

		// sync bi associations
		order.setOrderDetails(userOrderDTO.orderDetails());
		order.setCart(userOrderDTO.cart());

		// validate order
		//validator.setCurrentTime().validateUpdate(dbOrder);

		if (order.getCart() == null) {
			// cart is set to null if cartUpdateTimeLimit passed
			// in such case, set back the original cart
			Cart cart = new Cart();
			cart.setId(userOrderDTO.cart().getId());
			order.setCart(cart);
		}

		return orderRepository.updateUserOrder(order);
	}

	@Override
	public OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber) {
		int thePageSize = Integer.parseInt(pageSize);
		int thePageNumber = Integer.parseInt(pageNumber);
		int orderCount = orderRepository.findUserOrderCount(userId).intValue();

		int pageCount;
		if (orderCount % thePageSize == 0) {
			pageCount = orderCount / thePageSize;
		} else {
			pageCount = (orderCount / thePageSize) + 1;
		}

		Optional<List<OrderSummary>> orderSummaryList = orderRepository.findOrderSummaryList(userId, thePageSize, thePageNumber);

		return orderSummaryList.map(orderSummaries -> new OrderPaginationResultDTO(
				thePageNumber,
				pageCount,
				thePageSize,
				orderCount,
				orderSummaries)).orElse(null);
	}

	@Override
	public OrderDTO findUserOrder(String id) {
		return orderRepository.findUserOrder(id);
	}

	@Override
	public void deleteById(String id) {
		// get order createdOn
		OrderCreatedOnDTO createdOn = findCreatedOnById(id);
		// validate whatever delete time limit passed
		// validator.setCurrentTime().isOrderDeleteTimeLimitValid(createdOn.createdOn()); //FIXME - on for prod
		// delete order if time limit did not pass
		orderRepository.deleteById(id);
	}

	// NOTE - for internal use only

	@Override
	public Order findById(String id) {
		return orderRepository.findById(id);
	}

	@Override
	public OrderCreatedOnDTO findCreatedOnById(String id) {
		return orderRepository.findCreatedOnById(id);
	}
}
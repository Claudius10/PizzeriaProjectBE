package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import PizzaApp.api.entity.dto.order.*;
import PizzaApp.api.entity.dto.user.NewUserOrderDTO;
import PizzaApp.api.entity.dto.user.UpdateUserOrderDTO;
import PizzaApp.api.entity.order.Order;
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
		order.setUserData(null);

		// set created on in standard UTC (+00:00)
		order.setCreatedOn(LocalDateTime.now());

		// set formatted created on in UTC +2 since FE is +02:00
		order.setFormattedCreatedOn(order.getCreatedOn().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

		validator.validate(order);

		return orderRepository.createOrder(order);
	}

	@Override
	public Long createUserOrder(NewUserOrderDTO newUserOrderDTO) {
		Address dbAddress = addressService.findReference(String.valueOf(newUserOrderDTO.userOrderData().addressId()));
		UserData dbUserData = userDataService.findReference(newUserOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withCustomerName(newUserOrderDTO.userOrderData().customerName())
				.withContactTel(newUserOrderDTO.userOrderData().tel())
				.withEmail(newUserOrderDTO.userOrderData().email())
				.withOrderDetails(newUserOrderDTO.orderDetails())
				.withCart(newUserOrderDTO.cart())
				.withAddress(dbAddress)
				.build();

		dbUserData.addOrder(order); // also does order.setUserData(this);

		// validate order
		validator.validate(order);

		return orderRepository.createOrder(order);
	}

	@Override
	public Long updateUserOrder(UpdateUserOrderDTO updateUserOrderDTO) {
		Address dbAddress = addressService.findReference(String.valueOf(updateUserOrderDTO.userOrderData().addressId()));
		UserData dbUserData = userDataService.findReference(updateUserOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withUpdatedOn(LocalDateTime.now())
				.withFormattedUpdatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withId(updateUserOrderDTO.orderId())
				.withCreatedOn(updateUserOrderDTO.createdOn())
				.withFormattedCreatedOn(updateUserOrderDTO.formattedCreatedOn())
				.withCustomerName(updateUserOrderDTO.userOrderData().customerName())
				.withEmail(updateUserOrderDTO.userOrderData().email())
				.withContactTel(updateUserOrderDTO.userOrderData().tel())
				.withAddress(dbAddress)
				.withUser(dbUserData)
				.build();

		// sync bi associations
		order.setOrderDetails(updateUserOrderDTO.orderDetails());
		order.setCart(updateUserOrderDTO.cart());

		// validate order
		validator.setCurrentTime().validateUpdate(order);

		if (order.getCart() == null) {
			// cart is set to null if cartUpdateTimeLimit passed
			// in such case, set back the original cart
			order.setCart(orderRepository.findOrderCart(String.valueOf(order.getId())));
		}

		return orderRepository.updateOrder(order);
	}

	@Override
	public OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber) {
		int thePageSize = Integer.parseInt(pageSize);
		int thePageNumber = Integer.parseInt(pageNumber);
		int orderCount = orderRepository.findOrderCount(userId).intValue();

		int pageCount;
		if (orderCount % thePageSize == 0) {
			pageCount = orderCount / thePageSize;
		} else {
			pageCount = (orderCount / thePageSize) + 1;
		}

		return new OrderPaginationResultDTO(
				thePageNumber,
				pageCount,
				thePageSize,
				orderCount,
				orderRepository.findOrderSummaryList(userId, thePageSize, thePageNumber));
	}

	@Override
	public OrderDTO findUserOrder(String id) {
		return orderRepository.findOrder(id);
	}

	@Override
	public void deleteById(String id) {
		// get order createdOn
		OrderCreatedOnDTO createdOn = findCreatedOnById(id);
		// validate whatever delete time limit passed
		validator.setCurrentTime().isOrderDeleteTimeLimitValid(createdOn.createdOn()); //FIXME - on for prod
		// delete order if time limit did not pass
		orderRepository.deleteById(id);
	}

	// info - for internal use only

	@Override
	public Order findById(String id) {
		return orderRepository.findById(id);
	}

	@Override
	public OrderCreatedOnDTO findCreatedOnById(String id) {
		return orderRepository.findCreatedOnById(id);
	}

	@Override
	public Long createUserOrderTest(NewUserOrderDTO newUserOrderDTO, LocalDateTime createdOn) {
		Address dbAddress = addressService.findReference(String.valueOf(newUserOrderDTO.userOrderData().addressId()));
		UserData dbUserData = userDataService.findReference(newUserOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(createdOn)
				.withFormattedCreatedOn(createdOn.plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withCustomerName(newUserOrderDTO.userOrderData().customerName())
				.withContactTel(newUserOrderDTO.userOrderData().tel())
				.withEmail(newUserOrderDTO.userOrderData().email())
				.withOrderDetails(newUserOrderDTO.orderDetails())
				.withCart(newUserOrderDTO.cart())
				.withAddress(dbAddress)
				.build();

		dbUserData.addOrder(order); // also does order.setUserData(this);

		// validate order
		validator.validate(order);

		return orderRepository.createOrder(order);
	}
}
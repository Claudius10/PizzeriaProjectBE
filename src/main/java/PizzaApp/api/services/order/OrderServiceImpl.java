package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import PizzaApp.api.entity.dto.order.*;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.services.user.UserDataService;
import PizzaApp.api.services.address.AddressService;
import org.springframework.stereotype.Service;
import PizzaApp.api.repos.order.OrderRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final AddressService addressService;

	private final UserDataService userDataService;

	public OrderServiceImpl
			(OrderRepository orderRepository,
			 AddressService addressService,
			 UserDataService userDataService) {
		this.orderRepository = orderRepository;
		this.addressService = addressService;
		this.userDataService = userDataService;
	}

	@Override
	public String createAnonOrder(Order order) {
		Optional<Address> dbAddress = addressService.find(order.getAddress());
		dbAddress.ifPresent(order::setAddress);
		order.setUserData(null);

		// set created on in standard UTC (+00:00)
		order.setCreatedOn(LocalDateTime.now());

		// set formatted created on in UTC +1 cause db is in UTC
		order.setFormattedCreatedOn(order.getCreatedOn().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

		return orderRepository.createOrder(order);
	}

	@Override
	public String createUserOrder(UserOrderDTO userOrderDTO) {
		Address dbAddress = addressService.findReference(userOrderDTO.userOrderData().addressId());
		UserData dbUserData = userDataService.findReference(userOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withCustomerName(userOrderDTO.userOrderData().customerName())
				.withContactTel(userOrderDTO.userOrderData().tel())
				.withEmail(userOrderDTO.userOrderData().email())
				.withOrderDetails(userOrderDTO.orderDetails())
				.withCart(userOrderDTO.cart())
				.withAddress(dbAddress)
				.build();

		dbUserData.addOrder(order); // also does order.setUserData(this);
		return orderRepository.createOrder(order);
	}

	@Override
	public String updateUserOrder(UpdateUserOrderDTO updateUserOrderDTO) {
		Address dbAddress = addressService.findReference(updateUserOrderDTO.getUserOrderData().addressId());
		UserData dbUserData = userDataService.findReference(updateUserOrderDTO.getUserOrderData().userId());

		Order order = new Order.Builder()
				.withUpdatedOn(LocalDateTime.now())
				.withFormattedUpdatedOn(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withId(updateUserOrderDTO.getOrderId())
				.withCreatedOn(updateUserOrderDTO.getCreatedOn())
				.withFormattedCreatedOn(updateUserOrderDTO.getFormattedCreatedOn())
				.withCustomerName(updateUserOrderDTO.getUserOrderData().customerName())
				.withEmail(updateUserOrderDTO.getUserOrderData().email())
				.withContactTel(updateUserOrderDTO.getUserOrderData().tel())
				.withAddress(dbAddress)
				.withUser(dbUserData)
				.build();

		order.setOrderDetails(updateUserOrderDTO.getOrderDetails());
		// cart == null if !isCartUpdateValid
		if (updateUserOrderDTO.getCart() == null) {
			order.setCart(orderRepository.findOrderCart(order.getId()));
		} else {
			order.setCart(updateUserOrderDTO.getCart());
		}

		return orderRepository.updateOrder(order);
	}

	@Override
	public String deleteUserOrderById(Long orderId, Long userId) {
		UserData userData = userDataService.findReference(userId);
		userData.removeOrder(orderRepository.findReferenceById(orderId));
		return orderId.toString();
	}

	@Override
	public OrderPaginationResultDTO findUserOrdersSummary(Long userId, Integer pageSize, Integer pageNumber) {
		int totalOrders = orderRepository.findOrderCount(userId);

		int totalPages;
		if (totalOrders % pageSize == 0) {
			totalPages = totalOrders / pageSize;
		} else {
			totalPages = (totalOrders / pageSize) + 1;
		}

		return new OrderPaginationResultDTO(
				pageNumber,
				totalPages,
				pageSize,
				totalOrders,
				orderRepository.findOrderSummaryList(userId, pageSize, pageNumber));
	}

	@Override
	public OrderDTOPojo findUserOrderDTO(Long orderId) {
		return orderRepository.findOrderDTO(orderId);
	}

	// info - for internal use only

	@Override
	public Order findById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	@Override
	public void removeUserData(Long userId) {
		orderRepository.removeUserData(userId);
	}

	@Override
	public Order findReferenceById(Long orderId) {
		return orderRepository.findReferenceById(orderId);
	}

	@Override
	public OrderCreatedOnDTO findCreatedOnById(Long orderId) {
		return orderRepository.findCreatedOnById(orderId);
	}

	@Override
	public Cart findOrderCart(Long orderId) {
		return orderRepository.findOrderCart(orderId);
	}

	@Override
	public String createUserOrderTest(UserOrderDTO userOrderDTO, LocalDateTime createdOn) {
		Address dbAddress = addressService.findReference(userOrderDTO.userOrderData().addressId());
		UserData dbUserData = userDataService.findReference(userOrderDTO.userOrderData().userId());

		Order order = new Order.Builder()
				.withCreatedOn(createdOn)
				.withFormattedCreatedOn(createdOn.plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")))
				.withCustomerName(userOrderDTO.userOrderData().customerName())
				.withContactTel(userOrderDTO.userOrderData().tel())
				.withEmail(userOrderDTO.userOrderData().email())
				.withOrderDetails(userOrderDTO.orderDetails())
				.withCart(userOrderDTO.cart())
				.withAddress(dbAddress)
				.build();

		dbUserData.addOrder(order); // also does order.setUserData(this);
		return orderRepository.createOrder(order);
	}
}
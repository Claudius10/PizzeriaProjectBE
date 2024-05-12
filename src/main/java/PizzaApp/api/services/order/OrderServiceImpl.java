package PizzaApp.api.services.order;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.dto.*;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.repos.order.projections.OrderSummary;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final AddressService addressService;

	private final UserService userService;

	public OrderServiceImpl(OrderRepository orderRepository, AddressService addressService, UserService userService) {
		this.orderRepository = orderRepository;
		this.addressService = addressService;
		this.userService = userService;
	}

	@Override
	public OrderDTO findDTOById(Long orderId) {
		return new OrderDTO(findUserOrderById(orderId));
	}

	@Override
	public CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder) {
		Optional<Address> dbAddress = addressService.findByExample(newAnonOrder.address());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newAnonOrder.cart().getTotalQuantity())
				.withTotalCost(newAnonOrder.cart().getTotalCost())
				.withTotalCostOffers(newAnonOrder.cart().getTotalCostOffers())
				.withOrderItems(newAnonOrder.cart().getOrderItems())
				.build();

		Order anonOrder = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - " +
						"dd/MM/yyyy")))
				.withAnonCustomer(
						newAnonOrder.anonCustomerName(),
						newAnonOrder.anonCustomerContactNumber(),
						newAnonOrder.anonCustomerEmail())
				.withOrderDetails(newAnonOrder.orderDetails())
				.withCart(cart)
				.build();

		if (dbAddress.isPresent()) {
			anonOrder.setAddress(dbAddress.get());
		} else {
			anonOrder.setAddress(newAnonOrder.address());
		}

		Order order = orderRepository.save(anonOrder);
		return new CreatedAnonOrderDTO(
				order.getId(),
				order.getFormattedCreatedOn(),
				order.getAnonCustomerName(),
				order.getAnonCustomerContactNumber(),
				order.getAnonCustomerEmail(),
				order.getAddress(),
				order.getOrderDetails(),
				order.getCart()
		);
	}

	@Override
	public Long createUserOrder(NewUserOrderDTO newUserOrder) {
		User user = userService.findUserReference(newUserOrder.userId());
		Address address = addressService.findReference(newUserOrder.addressId());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newUserOrder.cart().getTotalQuantity())
				.withTotalCost(newUserOrder.cart().getTotalCost())
				.withTotalCostOffers(newUserOrder.cart().getTotalCostOffers())
				.withOrderItems(newUserOrder.cart().getOrderItems())
				.build();

		Order order = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - " +
						"dd/MM/yyyy")))
				.withUser(user)
				.withAddress(address)
				.withCart(cart)
				.withOrderDetails(newUserOrder.orderDetails())
				.build();

		return orderRepository.save(order).getId();
	}

	@Override
	public Long updateUserOrder(UpdateUserOrderDTO updateUserOrder) {
		Address address = addressService.findUserAddressById(updateUserOrder.getAddressId());
		Order order = findUserOrderById(updateUserOrder.getOrderId());

		boolean pendingAddressUpdate = !order.getAddress().contentEquals(address);
		boolean pendingOrderDetailsUpdate = !order.getOrderDetails().contentEquals(updateUserOrder.getOrderDetails());
		boolean pendingCartUpdate = !order.getCart().contentEquals(updateUserOrder.getCart()) && updateUserOrder.getCart() != null;

		if (pendingAddressUpdate) {
			order.setAddress(address);
		}

		if (pendingOrderDetailsUpdate) {
			order.getOrderDetails().setDeliveryHour(updateUserOrder.getOrderDetails().getDeliveryHour());
			order.getOrderDetails().setPaymentType(updateUserOrder.getOrderDetails().getPaymentType());
			order.getOrderDetails().setChangeRequested(updateUserOrder.getOrderDetails().getChangeRequested());
			order.getOrderDetails().setPaymentChange(updateUserOrder.getOrderDetails().getPaymentChange());
			order.getOrderDetails().setDeliveryComment(updateUserOrder.getOrderDetails().getDeliveryComment());
		}

		// cart == null if !isCartUpdateValid
		if (pendingCartUpdate) {
			order.getCart().setTotalQuantity(updateUserOrder.getCart().getTotalQuantity());
			order.getCart().setTotalCost(updateUserOrder.getCart().getTotalCost());
			order.getCart().setTotalCostOffers(updateUserOrder.getCart().getTotalCostOffers());
			order.getCart().getOrderItems().clear();
			for (OrderItem item : updateUserOrder.getCart().getOrderItems()) {
				order.getCart().addItem(item);
			}
		}

		if (pendingAddressUpdate || pendingOrderDetailsUpdate || pendingCartUpdate) {
			order.setUpdatedOn(LocalDateTime.now());
			order.setFormattedUpdatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - " +
					"dd/MM/yyyy")));
		}

		return order.getId();
	}

	@Override
	public Long deleteUserOrderById(Long orderId) {
		orderRepository.delete(findUserOrderById(orderId));
		return orderId;
	}

	@Override
	public Page<OrderSummary> findUserOrderSummary(Long userId, int size, int page) {
		Sort.TypedSort<Order> order = Sort.sort(Order.class);
		Sort sort = order.by(Order::getId).descending();
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		return orderRepository.findAllByUser_Id(userId, pageRequest);
	}

	// info - for internal use only

	@Override
	public Order findUserOrderById(Long orderId) {
		return orderRepository.findUserOrderById(orderId);
	}

	@Override
	public LocalDateTime findCreatedOnById(Long orderId) {
		return orderRepository.findCreatedOnById(orderId).createdOn();
	}
}
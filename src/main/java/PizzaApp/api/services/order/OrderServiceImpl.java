package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.repos.order.projections.OrderSummary;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.user.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

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
		return new OrderDTO(findByIdNoLazy(orderId));
	}

	@Override
	public Long createAnonOrder(NewAnonOrderDTO newAnonOrder) {

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newAnonOrder.cart().getTotalQuantity())
				.withTotalCost(newAnonOrder.cart().getTotalCost())
				.withTotalCostOffers(newAnonOrder.cart().getTotalCostOffers())
				.withOrderItems(newAnonOrder.cart().getOrderItems())
				.build();

		Order anonOrder = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - " +
						"dd/MM/yyyy")))
				.withAnonCustomer(
						newAnonOrder.anonCustomerName(),
						newAnonOrder.anonCustomerContactNumber(),
						newAnonOrder.anonCustomerEmail())
				.withAddress(newAnonOrder.address())
				.withOrderDetails(newAnonOrder.orderDetails())
				.withCart(cart)
				.build();

		Optional<Address> dbAddress = addressService.findByExample(newAnonOrder.address());
		dbAddress.ifPresent(anonOrder::setAddress);

		return orderRepository.save(anonOrder).getId();
	}

	@Override
	public Long createUserOrder(NewUserOrderDTO newUserOrder) {
		User user = userService.findReference(newUserOrder.userId());
		Address address = addressService.findReference(newUserOrder.addressId());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newUserOrder.cart().getTotalQuantity())
				.withTotalCost(newUserOrder.cart().getTotalCost())
				.withTotalCostOffers(newUserOrder.cart().getTotalCostOffers())
				.withOrderItems(newUserOrder.cart().getOrderItems())
				.build();

		Order order = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withFormattedCreatedOn(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - " +
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
		Address address = addressService.findAddressById(updateUserOrder.getAddressId());
		Order order = findByIdNoLazy(updateUserOrder.getOrderId());

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
			order.setFormattedUpdatedOn(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm - " +
					"dd/MM/yyyy")));
		}

		return order.getId();
	}

	@Override
	public Long deleteUserOrderById(Long orderId) {
		orderRepository.delete(findByIdNoLazy(orderId));
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
	public Order findByIdNoLazy(Long orderId) {
		return orderRepository.findByIdNoLazy(orderId);
	}

	@Override
	public LocalDateTime findCreatedOnById(Long orderId) {
		return orderRepository.findCreatedOnById(orderId).getCreatedOn();
	}
}
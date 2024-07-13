package org.pizzeria.api.services.order;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.entity.order.OrderItem;
import org.pizzeria.api.entity.order.dto.*;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.repos.order.OrderRepository;
import org.pizzeria.api.repos.order.projections.CreatedOnOnly;
import org.pizzeria.api.repos.order.projections.OrderSummary;
import org.pizzeria.api.services.address.AddressService;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	public Optional<OrderDTO> findDTOById(Long orderId) {
		Optional<Order> order = findUserOrderById(orderId);
		return order.map(OrderDTO::new);
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
		User user = userService.findUserOrThrow(newUserOrder.userId());
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
		Long id = null;
		Optional<Address> dbAddress = addressService.findAddressById(updateUserOrder.getAddressId());
		Optional<Order> dbOrder = findUserOrderById(updateUserOrder.getOrderId());

		if (dbOrder.isPresent() && dbAddress.isPresent()) {
			Address address = dbAddress.get();
			Order order = dbOrder.get();

			boolean pendingAddressUpdate = !order.getAddress().contentEquals(address);
			boolean pendingOrderDetailsUpdate = !order.getOrderDetails().contentEquals(updateUserOrder.getOrderDetails());
			boolean pendingCartUpdate = updateUserOrder.getCart() != null && !order.getCart().contentEquals(updateUserOrder.getCart());

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

			id = order.getId();
		}

		return id;
	}

	@Override
	public Long deleteUserOrderById(Long orderId) {
		Long id = null;
		Optional<Order> dbOrder = findUserOrderById(orderId);

		if (dbOrder.isPresent()) {
			orderRepository.delete(dbOrder.get());
			id = orderId;
		}

		return id;
	}

	@Override
	public Page<OrderSummary> findUserOrderSummary(Long userId, int size, int page) {
		if (!userService.existsById(userId)) {
			throw new UsernameNotFoundException(String.format(SecurityResponses.USER_NOT_FOUND, userId));
		}

		Sort.TypedSort<Order> order = Sort.sort(Order.class);
		Sort sort = order.by(Order::getId).descending();
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		return orderRepository.findAllByUser_Id(userId, pageRequest);
	}

	// for internal use only

	@Override
	public Optional<Order> findUserOrderById(Long orderId) {
		return orderRepository.findUserOrderById(orderId);
	}

	@Override
	public LocalDateTime findCreatedOnById(Long orderId) {
		Optional<CreatedOnOnly> createdOn = orderRepository.findCreatedOnById(orderId);
		return createdOn.map(CreatedOnOnly::createdOn).orElse(null);
	}
}
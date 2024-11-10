package org.pizzeria.api.services.order;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.cart.CartItem;
import org.pizzeria.api.entity.order.Order;
import org.pizzeria.api.entity.order.dto.*;
import org.pizzeria.api.entity.order.projections.OrderSummaryProjection;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.repos.order.OrderRepository;
import org.pizzeria.api.services.address.AddressService;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.utils.globals.ApiResponses;
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
	public Optional<OrderDTO> findProjectionById(Long orderId) {
		return orderRepository.findOrderById(orderId);
	}

	@Override
	public CreatedAnonOrderDTO createAnonOrder(NewAnonOrderDTO newAnonOrder) {
		Optional<Address> dbAddress = addressService.findByExample(newAnonOrder.address());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newAnonOrder.cart().getTotalQuantity())
				.withTotalCost(newAnonOrder.cart().getTotalCost())
				.withTotalCostOffers(newAnonOrder.cart().getTotalCostOffers())
				.withCartItems(newAnonOrder.cart().getCartItems())
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
	public Long createUserOrder(Long userId, NewUserOrderDTO newUserOrder) {
		User user = userService.findUserOrThrow(userId);
		Address address = addressService.findReference(newUserOrder.addressId());

		Cart cart = new Cart.Builder()
				.withTotalQuantity(newUserOrder.cart().getTotalQuantity())
				.withTotalCost(newUserOrder.cart().getTotalCost())
				.withTotalCostOffers(newUserOrder.cart().getTotalCostOffers())
				.withCartItems(newUserOrder.cart().getCartItems())
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
	public Long updateUserOrder(Long orderId, UpdateUserOrderDTO updateUserOrder) {
		Optional<Address> dbAddress = addressService.findAddressById(updateUserOrder.addressId());
		Optional<Order> dbOrder = findUserOrderById(orderId);

		if (dbOrder.isPresent() && dbAddress.isPresent()) {
			Address address = dbAddress.get();
			Order order = dbOrder.get();

			boolean pendingAddressUpdate = !order.getAddress().contentEquals(address);
			boolean pendingOrderDetailsUpdate = !order.getOrderDetails().contentEquals(updateUserOrder.orderDetails());
			boolean pendingCartUpdate = updateUserOrder.cart() != null && !order.getCart().contentEquals(updateUserOrder.cart());

			if (pendingAddressUpdate) {
				order.setAddress(address);
			}

			if (pendingOrderDetailsUpdate) {
				order.getOrderDetails().setDeliveryTime(updateUserOrder.orderDetails().getDeliveryTime());
				order.getOrderDetails().setPaymentMethod(updateUserOrder.orderDetails().getPaymentMethod());
				order.getOrderDetails().setBillToChange(updateUserOrder.orderDetails().getBillToChange());
				order.getOrderDetails().setChangeToGive(updateUserOrder.orderDetails().getChangeToGive());
				order.getOrderDetails().setComment(updateUserOrder.orderDetails().getComment());
			}

			// cart == null if !isCartUpdateValid
			if (pendingCartUpdate) {
				order.getCart().setTotalQuantity(updateUserOrder.cart().getTotalQuantity());
				order.getCart().setTotalCost(updateUserOrder.cart().getTotalCost());
				order.getCart().setTotalCostOffers(updateUserOrder.cart().getTotalCostOffers());
				order.getCart().getCartItems().clear();
				for (CartItem item : updateUserOrder.cart().getCartItems()) {
					order.getCart().addItem(item);
				}
			}

			if (pendingAddressUpdate || pendingOrderDetailsUpdate || pendingCartUpdate) {
				order.setUpdatedOn(LocalDateTime.now());
				order.setFormattedUpdatedOn(LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm - " +
						"dd/MM/yyyy")));
			}

			return orderId;
		}

		return null;
	}

	@Override
	public Long deleteUserOrderById(Long orderId) {
		Optional<Order> dbOrder = findUserOrderById(orderId);
		dbOrder.ifPresent(orderRepository::delete);
		return orderId;
	}

	@Override
	public Page<OrderSummaryProjection> findUserOrderSummary(Long userId, int size, int page) {
		if (!userService.existsById(userId)) {
			throw new UsernameNotFoundException(String.format(ApiResponses.USER_NOT_FOUND, userId));
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
		Optional<CreatedOnDTO> createdOn = orderRepository.findCreatedOnById(orderId);
		return createdOn.map(CreatedOnDTO::createdOn).orElse(null);
	}
}
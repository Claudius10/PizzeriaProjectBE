package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import PizzaApp.api.entity.dto.order.OrderPaginationResultDTO;
import PizzaApp.api.entity.dto.user.UserOrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.services.user.account.AccountService;
import PizzaApp.api.services.user.address.AddressService;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.validation.order.OrderValidator;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final OrderRepository orderRepository;

	private final AddressService addressService;

	private final AccountService accountService;

	private final OrderValidator validator;

	public OrderServiceImpl(
			OrderRepository orderRepository,
			AddressService addressService,
			AccountService accountService,
			OrderValidator validator) {
		this.orderRepository = orderRepository;
		this.addressService = addressService;
		this.accountService = accountService;
		this.validator = validator;
	}

	@Override
	public Long createAnonOrder(Order order) {
		Optional<Address> dbAddress = addressService.findAddress(order.getAddress());
		dbAddress.ifPresent(order::setAddress);
		order.setUserData(null);

		// prevent HB from performing unnecessary operations
		for (OrderItem item : order.getCart().getOrderItems()) {
			item.setId(null);
		}
		return orderRepository.createOrder(order);
	}

	@Override
	public Long createUserOrder(UserOrderDTO userOrderDTO) {
		Address dbAddress = addressService.findReference(userOrderDTO.userOrderData().addressId());
		UserData dbUserData = accountService.findReference(userOrderDTO.userOrderData().userId());

		// TODO - make an util method of this
		// remove id's from items that come from front end
		// so Hibernate doesn't perform extra unnecessary operations
		for (OrderItem item : userOrderDTO.cart().getOrderItems()) {
			item.setId(null);
		}

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

		// validate order
		validator.validate(order);

		return orderRepository.createOrder(order);
	}

	@Override
	public Long updateUserOrder(UserOrderDTO userOrderDTO) {
		Address dbAddress = addressService.findReference(userOrderDTO.userOrderData().addressId());
		UserData dbUserData = accountService.findReference(userOrderDTO.userOrderData().userId());

		for (OrderItem item : userOrderDTO.cart().getOrderItems()) {
			item.setId(null);
		}

		Order dbOrder = new Order.Builder()
				.withCreatedOn(orderRepository.findCreatedOnById(userOrderDTO.orderId()))
				.withUpdatedOn(LocalDateTime.now())
				.withId(userOrderDTO.orderId())
				.withUser(dbUserData)
				.withAddress(dbAddress)
				.withCustomerName(userOrderDTO.userOrderData().customerName())
				.withEmail(userOrderDTO.userOrderData().email())
				.withContactTel(userOrderDTO.userOrderData().tel())
				.build();
		dbOrder.setOrderDetails(userOrderDTO.orderDetails());
		dbOrder.setCart(userOrderDTO.cart());

		// validate order
		//validator.setCurrentTime().validateUpdate(dbOrder);

		if (dbOrder.getCart() == null) {
			// cart is set to null if now is after cartUpdateTimeLimit
			// in such case, set back the original
			Cart dbCart = new Cart();
			dbCart.setId(userOrderDTO.cart().getId());
			dbOrder.setCart(dbCart);
		}

		List<OrderItem> copy = new ArrayList<>(dbOrder.getCart().getOrderItems());
		dbOrder.getCart().getOrderItems().clear();

		for (OrderItem item : copy) {
			dbOrder.getCart().addItem(item);
		}

		return orderRepository.updateUserOrder(dbOrder);
	}

	/*public Long createOrUpdate(Order order) {
		// check for email and address in db
		// FIXME - find a better solution for the orderDataInternalService
		OrderData dbOrderData = orderDataInternalService.findOrderData(order);

		// for create check whatever there userData is not null
		// if it isn't, search address by ID (mby tel too)

		// create
		if (order.getId() == null) {

			if (dbOrderData.getAddress() != null) {
				order.getAddress().setId(dbOrderData.getAddress().getId());
			}

			// remove id's from items that come from front end
			// so Hibernate doesn't perform extra unnecessary operations
			for (OrderItem item : order.getCart().getOrderItems()) {
				item.setId(null);
			}

		} else {
			// update

			// get original order data
			logger.info("UPDATE: SEARCHING ORIGINAL ORDER");
			Order originalOrder = findById(order.getId());

			// setting originalCreatedOn here, not on the front-end
			order.setCreatedOn(originalOrder.getCreatedOn());

			// validation for updating
			validator.setCurrentTime().validateMutation(order);

			// CUSTOMER

			// if not updating customer data, set the same one from db
			if (order.getCustomerName() == null) {
				order.setCustomerName(originalOrder.getCustomerName());
			}

			if (order.getContactTel() == null) {
				order.setContactTel(originalOrder.getContactTel());
			}

			if (order.getEmail() == null) {
				order.setEmail(originalOrder.getEmail());
			}

			// DELIVERY

			// check whatever updating address
			// if it's not being updated, set it
			if (order.getAddress() == null) {
				order.setAddress(originalOrder.getAddress());
			} else {
				if (dbOrderData.getAddress() != null) {
					order.getAddress().setId(dbOrderData.getAddress().getId());
				}
			}

			// ORDER DETAILS

			// check if updating orderDetails
			// if null (not updating) set from db
			if (order.getOrderDetails() == null) {
				order.setOrderDetails(originalOrder.getOrderDetails());
			} else {
				// if it's being updated, set the id
				order.getOrderDetails().setId(originalOrder.getId());
			}

			// CART
			if (order.getCart() == null) {
				// cart is set to null if now is after cartUpdateTimeLimit
				// set back the original
				order.setCart(originalOrder.getCart());
			} else {
				order.getCart().setId(originalOrder.getId());
			}
		}

		// validation
		validator.validate(order);

		// create or update order
		logger.info("SAVING ORDER TO DB");
		return orderRepository.createOrUpdate(order);
	}*/

	@Override
	public OrderPaginationResultDTO findOrdersSummary(String userId, String pageSize, String pageNumber) {
		return orderRepository.findOrdersSummary(
				Long.parseLong(userId),
				Integer.parseInt(pageSize),
				Integer.parseInt(pageNumber));
	}

	@Override
	public OrderDTO findUserOrder(String id) {

		OrderDTO order = orderRepository.findUserOrder(id);

		// TODO - create an utility method to format createdOn and updatedOn
		if (order != null) {

			// when setting createdOn
			// add +2 hours since db is in standard UTC (+00:00)
			// and front-end is in +02:00
			order.setCreatedOn(order.getCreatedOn().plusHours(2));

			// format createdOn
			order.setFormattedCreatedOn(order.getCreatedOn().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

			// format updatedOn if it exists
			if (order.getUpdatedOn() != null) {

				order.setUpdatedOn(order.getUpdatedOn().plusHours(2));
				order.setFormattedUpdatedOn(order.getUpdatedOn().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));
			}

			return order;
		} else {
			throw new NoResultException(
					"El pedido " + id + " no se pudo encontrar");
		}
	}

	@Override
	public void deleteById(Long id) {
		// get order createdOn
		LocalDateTime createdOn = findCreatedOnById(id);
		// validate whatever delete time limit passed
		// validator.setCurrentTime().isOrderDeleteTimeLimitValid(createdOn); //FIXME - on for prod
		// delete order if time limit did not pass
		orderRepository.deleteById(id);
	}

	// internal use

	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public LocalDateTime findCreatedOnById(Long id) {
		return orderRepository.findCreatedOnById(id);
	}

}
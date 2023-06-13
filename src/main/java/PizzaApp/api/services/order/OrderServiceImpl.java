package PizzaApp.api.services.order;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.utility.order.OrderData;
import PizzaApp.api.utility.order.OrderDataInternalService;
import PizzaApp.api.validation.order.OrderValidation;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final Logger logger = Logger.getLogger(getClass().getName());
	private final OrderRepository orderRepository;
	private final OrderDataInternalService orderDataInternalService;
	private final OrderValidation validator;

	public OrderServiceImpl(OrderRepository orderRepository, OrderDataInternalService orderDataInternalService,
							OrderValidation validator) {
		this.orderRepository = orderRepository;
		this.orderDataInternalService = orderDataInternalService;
		this.validator = validator;
	}

	@Override
	public Long createOrUpdate(Order order) {
		// check for email and address in db
		OrderData dbOrderData = orderDataInternalService.findOrderData(order);

		// create
		if (order.getId() == null) {

			// if address is already in db, set it
			// to not have duplicates
			if (dbOrderData.getAddress() != null) {
				order.getAddress().setId(dbOrderData.getAddress().getId());
			}

			// same for email
			if (dbOrderData.getEmail() != null) {
				order.getEmail().setId(dbOrderData.getEmail().getId());
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
			if (order.getCustomerFirstName() == null) {
				order.setCustomerFirstName(originalOrder.getCustomerFirstName());
			}

			if (order.getCustomerLastName() == null && originalOrder.getCustomerLastName() != null) {
				order.setCustomerLastName(originalOrder.getCustomerLastName());
			}

			if (order.getContactTel() == null) {
				order.setContactTel(originalOrder.getContactTel());
			}

			if (order.getEmail() == null) {
				order.setEmail(originalOrder.getEmail());
			} else {
				if (dbOrderData.getEmail() != null) {
					order.getEmail().setId(dbOrderData.getEmail().getId());
				}
			}

			// DELIVERY

			// check whatever updating address
			// if it's not being updated, set it
			if (order.getAddress() == null) {
				order.setAddress(originalOrder.getAddress());
			} else {
				// if it is, set its id if it's the db
				// else insert the new one
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
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public OrderDTO findDTOByIdAndTel(String id, String orderContactTel) {

		OrderDTO order = orderRepository.findDTOByIdAndTel(id, orderContactTel);

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
					"El pedido " + id + " asociado al teléfono " + orderContactTel + " no se pudo encontrar");
		}
	}

	@Override
	public OrderCreatedOnDTO findCreatedOnById(Long id) {
		return orderRepository.findCreatedOnById(id);
	}

	@Override
	public void deleteById(Long id) {
		// get order createdOn
		OrderCreatedOnDTO order = findCreatedOnById(id);
		// validate whatever delete time limit passed
		validator.setCurrentTime().isOrderDeleteTimeLimitValid(order.getCreatedOn());
		// delete order if time limit did not pass
		orderRepository.deleteById(id);
	}
}
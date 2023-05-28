package PizzaApp.api.services.order;

import java.time.LocalDateTime;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.email.EmailService;
import PizzaApp.api.services.telephone.TelephoneService;
import PizzaApp.api.utility.order.OrderData;
import PizzaApp.api.utility.order.OrderDataInternalService;
import PizzaApp.api.utility.order.OrderUtility;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private OrderDataInternalService orderDataInternalService;
	private OrderUtility orderUtility;
	private final Logger logger = Logger.getLogger(getClass().getName());

	public OrdersServiceImpl(OrderRepository orderRepository, OrderDataInternalService orderDataInternalService,
			AddressService addressService, TelephoneService telephoneService, EmailService emailService,
			OrderUtility orderUtility) {
		this.orderRepository = orderRepository;
		this.orderDataInternalService = orderDataInternalService;
		this.orderUtility = orderUtility;
	}

	@Override
	public Order createOrUpdate(Order order) {
		// get UTC+2 date and time
		LocalDateTime now = LocalDateTime.now().plusHours(2);

		// check for customer, email, tel, and address in db
		OrderData dbOrderData = orderDataInternalService.findOrderData(order);

		// create
		if (order.getId() == null) {

			// set the current date
			order.setCreatedOn(now);

			// if address is already in db, set it
			// to not have duplicates
			if (dbOrderData.getAddress() != null) {
				order.getAddress().setId(dbOrderData.getAddress().getId());
			}

			// same for email
			if (dbOrderData.getEmail() != null) {
				order.getEmail().setId(dbOrderData.getEmail().getId());
			}

		} else {
			// update

			// get original order data
			logger.info("UPDATE: SEARCHING ORIGINAL ORDER");
			Order originalOrder = findById(order.getId());

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

			// ORDERDETAILS

			// check if updating orderDetails
			// if null (not updating) set from db
			if (order.getOrderDetails() == null) {
				order.setOrderDetails(originalOrder.getOrderDetails());
			} else {
				// if it's being updated, set the id
				order.getOrderDetails().setId(originalOrder.getOrderDetails().getId());
			}

			// CART

			// set the cart if cart is not being updated
			if (order.getCart() == null) {
				order.setCart(originalOrder.getCart());
			} else {
				order.getCart().setId(originalOrder.getCart().getId());
			}

			// set created on
			order.setCreatedOn(originalOrder.getCreatedOn());
			// set updated on
			order.setUpdatedOn(now);
		}

		// validation
		orderUtility.validate(order);

		// create or update order
		logger.info("SAVING ORDER TO DB");
		return orderRepository.createOrUpdate(order);
	}

	@Override
	public Order findById(Long id) {
		Order order = orderRepository.findById(id);

		if (order != null) {
			return order;
		} else {
			throw new NoResultException("Pedido " + id + " no se pudo encontrar");
		}
	}

	@Override
	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}
}

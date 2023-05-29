package PizzaApp.api.services.order;

import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.utility.order.OrderData;
import PizzaApp.api.utility.order.OrderUpdateUtility;
import PizzaApp.api.utility.order.interfaces.OrderDataInternalService;
import PizzaApp.api.utility.order.interfaces.OrderUtility;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersServiceImpl implements OrderService {

	private final Logger logger = Logger.getLogger(getClass().getName());
	private OrderRepository orderRepository;
	private OrderDataInternalService orderDataInternalService;
	private OrderUtility orderUtility;
	private OrderUpdateUtility orderUpdateUtility;

	public OrdersServiceImpl(OrderRepository orderRepository, OrderDataInternalService orderDataInternalService,
			OrderUtility orderUtility, OrderUpdateUtility orderUpdateUtility) {
		this.orderRepository = orderRepository;
		this.orderDataInternalService = orderDataInternalService;
		this.orderUtility = orderUtility;
		this.orderUpdateUtility = orderUpdateUtility;
	}

	@Override
	public Order createOrUpdate(Order order) {
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

		} else {
			// update

			// get original order data
			logger.info("UPDATE: SEARCHING ORIGINAL ORDER");
			Order originalOrder = findById(order.getId());
			order.setCreatedOn(originalOrder.getCreatedOn());

			// validation for updating
			if (order.getCart() == null) {
				orderUpdateUtility.get(order.getCreatedOn()).validate();
			} else {
				orderUpdateUtility.get(order.getCreatedOn(), order.getCart()).validate();
			}

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
		}

		// validation
		orderUtility.validate(order);

		// create or update order
		logger.info("SAVING ORDER TO DB");
		return orderRepository.createOrUpdate(order);
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public OrderDTO findDTOById(Long id) {
		OrderDTO order = orderRepository.findDTOById(id);

		if (order != null) {
			return order;
		} else {
			throw new NoResultException("Pedido " + id + " no se pudo encontrar");
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
		orderUpdateUtility.get(order.getCreatedOn()).validateDelete();
		// delete order if time limit did not pass
		orderRepository.deleteById(id);
	}
}
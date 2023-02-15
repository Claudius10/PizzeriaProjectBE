package PizzaApp.api.repos;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.Address;
import PizzaApp.api.entity.Customer;
import PizzaApp.api.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class OrdersRepository {

	private EntityManager em;

	public OrdersRepository(EntityManager em) {
		this.em = em;
	}

	// create / update
	public void createOrder(Order order) {
		String orderDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));
		// create
		if (order.getId() == 0) {

			order.getOrderDetails().setOrderDate(orderDate);

			Customer dbCustomer = findCustomerByTel(order.getCustomer().getTel());

			if (dbCustomer != null) {
				dbCustomer.setFirstName(order.getCustomer().getFirstName());
				dbCustomer.setLastName(order.getCustomer().getLastName());
				dbCustomer.setEmail(order.getCustomer().getEmail());
				order.setCustomer(dbCustomer);
			}

			if (order.getAddress() != null) {
				Address dbAddress = findAddress(order.getAddress().getStreet(), order.getAddress().getStreetNr(),
						order.getAddress().getGate(), order.getAddress().getStaircase(), order.getAddress().getFloor(),
						order.getAddress().getDoor());
				if (dbAddress != null) {
					order.setAddress(dbAddress);
				}
			}

			Order newOrder = em.merge(order);
			order.setId(newOrder.getId());

		} else {
			// update
			Order dbOrder = findOrderById(order.getId());

			// if orderDetails are not update
			// set them from DB to be able to set order update date
			if (order.getOrderDetails() == null) {
				order.setOrderDetails(dbOrder.getOrderDetails());
				order.getOrderDetails().setOrderDate(orderDate);
			} else {
				order.getOrderDetails().setOrderDate(orderDate);
			}

			// there can be either an address for home delivery
			// or a storePickUpName
			// not both

			// update order from home delivery to store pickup
			// home delivery => store pick up update
			if (dbOrder.getAddress() != null && order.getStorePickUpName() != null) {
				order.setAddress(null);
			}

			// update order from storePickUp to home delivery
			// store pick up => home delivery update
			if (dbOrder.getStorePickUpName() != null && order.getAddress() != null) {
				order.setStorePickUpName(null);

				// check whatever address is already in db
				Address dbAddress = findAddress(order.getAddress().getStreet(), order.getAddress().getStreetNr(),
						order.getAddress().getGate(), order.getAddress().getStaircase(), order.getAddress().getFloor(),
						order.getAddress().getDoor());

				if (dbAddress != null) {
					// if it is, don't add it again
					order.setAddress(dbAddress);
				}

			}

			// if customer prop is not set on the FE obj cause it's not being updated
			// set the one from the DB so when order is updated
			// customer_id doesn't go to NULL
			if (dbOrder.getCustomer() != null) {
				order.setCustomer(dbOrder.getCustomer());
			}

			Order updatedOrder = em.merge(order);
			order.setId(updatedOrder.getId());
		}
	}

	// read
	public List<Order> getOrders() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public Order findOrderById(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> getOrdersByStore(String storeName) {
		TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.storePickUpName=:storeName", Order.class);
		query.setParameter("storeName", storeName);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> getOrdersByCustomer(Long customerId) {
		TypedQuery<Order> query = em.createQuery("from Order o WHERE o.customer.id=:customerId", Order.class);
		query.setParameter("customerId", customerId);
		List<Order> customerOrders = query.getResultList();
		return customerOrders;
	}

	// update

	// delete

	////////////////

	public List<Customer> getCustomers() {
		TypedQuery<Customer> query = em.createQuery("from Customer", Customer.class);
		List<Customer> customers = query.getResultList();
		return customers;
	}

	public List<Address> getAddress() {
		TypedQuery<Address> query = em.createQuery("from Address", Address.class);
		List<Address> address = query.getResultList();
		return address;
	}

	public Address findAddress(String oStreet, int oNumber, String oGate, String oStaircase, String oFloor,
			String oDoor) {
		TypedQuery<Address> query = em
				.createQuery(
						"from Address a where a.street=:oStreet " + "and a.streetNr=:oNumber " + "and a.gate=:oGate "
								+ "and a.staircase=:oStaircase " + "and a.floor=:oFloor " + "and a.door=:oDoor",
						Address.class);
		query.setParameter("oStreet", oStreet);
		query.setParameter("oNumber", oNumber);
		query.setParameter("oGate", oGate);
		query.setParameter("oStaircase", oStaircase);
		query.setParameter("oFloor", oFloor);
		query.setParameter("oDoor", oDoor);

		Address dbAddress = null;
		try {
			dbAddress = query.getSingleResult();
		} catch (NoResultException nre) {
		}
		return dbAddress;
	}

	public Customer findCustomerByTel(int custTel) {
		TypedQuery<Customer> query = em.createQuery("from Customer c where c.tel=:custTel", Customer.class);
		query.setParameter("custTel", custTel);

		Customer dbCustomer = null;
		try {
			dbCustomer = query.getSingleResult();
		} catch (NoResultException nre) {
		}
		return dbCustomer;
	}
}

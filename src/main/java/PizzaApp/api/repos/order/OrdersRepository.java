package PizzaApp.api.repos.order;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Customer;
import PizzaApp.api.entity.order.Order;
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
		// new order
		if (order.getId() == 0) {

			order.getOrderDetails().setOrderDate(orderDate);

			// find if customer of new order is in db
			Customer dbCustomer = findCustomerByTel(order.getCustomer().getTel());

			if (dbCustomer != null) {
				order.getCustomer().setId(dbCustomer.getId());
			}
			// if db is already in db, do not INSERT again
			if (order.getAddress() != null) {
				// check whatever address to update is already in db
				Address dbAddress = findAddress(order.getAddress().getStreet(), order.getAddress().getStreetNr(),
						order.getAddress().getGate(), order.getAddress().getStaircase(), order.getAddress().getFloor(),
						order.getAddress().getDoor());
				if (dbAddress != null) {
					order.setAddress(dbAddress);
				}
			}

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
			// or a storePickUpName; not both
			if (order.getAddress() != null) {

				Address dbAddress = findAddress(order.getAddress().getStreet(), order.getAddress().getStreetNr(),
						order.getAddress().getGate(), order.getAddress().getStaircase(), order.getAddress().getFloor(),
						order.getAddress().getDoor());

				// update
				// store pick up => home delivery update
				if (dbOrder.getStorePickUpName() != null) {
					order.setStorePickUpName(null);
				}

				// when updating from store pick up to home delivery
				// if incoming address is not in db
				// do a INSERT, not UPDATE on the address with the old db
				// from when it was fetched on the FE to proceed with the update
				if (dbAddress == null) {
					order.getAddress().setId(null);
				} else {
					order.setAddress(dbAddress);
				}
				// if neither the address or store pick up is being updated
			} else if (order.getAddress() == null && order.getStorePickUpName() == null) {
				// but order to update has home delivery address
				if (dbOrder.getStorePickUpName() == null) {
					order.setAddress(dbOrder.getAddress());
					// or order to update has store pick up name
				} else {
					order.setStorePickUpName(dbOrder.getStorePickUpName());
				}
			}

			// update order from home delivery to store pickup
			// home delivery => store pick up update
			if (dbOrder.getAddress() != null && order.getStorePickUpName() != null) {
				order.setAddress(null);
			}

			// if customer prop is not set on the FE obj cause it's not being updated
			// set the one from the DB so when order is updated
			// customer_id doesn't go to NULL
			if (order.getCustomer() == null) {
				order.setCustomer(dbOrder.getCustomer());
			} else {
				Customer dbCustomer = findCustomerByTel(order.getCustomer().getTel());
				// when updating customer, the newly entered tel is already in db
				if (dbCustomer != null && dbCustomer.getTel() == order.getCustomer().getTel()) {
					// then update the customer who'se tel was already in db
					// and not INSERT a new customer with the same tel
					order.getCustomer().setId(dbCustomer.getId());
				}
			}
		}
		Order theOrder = em.merge(order);
		order.setId(theOrder.getId());
	}

	// read

	public Order findOrderById(Long id) {
		Order order = em.find(Order.class, id);
		if (order != null) {
			return order;
		} else {
			throw new NoResultException("Pedido " + id + " no se pudo encontrar.");
		}
	}

	// can refact this to have its param as an Address obj and then use it inside to
	// get prop values
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

	// delete
	public void deleteOrderById(Long id) {
		Order dbOrderToDelete = findOrderById(id);
		em.remove(dbOrderToDelete);
	}

	// currently not in use

	public List<Order> getOrders() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
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
}

package PizzaApp.api.repos;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.Address;
import PizzaApp.api.entity.Customer;
import PizzaApp.api.entity.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
/*
@Repository
public class OrdersRepositoryOld {

	private EntityManager em;

	public OrdersRepositoryOld(EntityManager em) {
		this.em = em;
	}

	public void createOrder(Order order) {
		// String orderDate =
		// ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));
		// order.getOrderDetails().setOrderDate(orderDate);

		Address orderAddress = order.getCustomer().getAddressList().get(0);
		Address dbAddress = findAddress(orderAddress.getStreet(), orderAddress.getStreetNr(), orderAddress.getGate(),
				orderAddress.getStaircase(), orderAddress.getFloor(), orderAddress.getDoor());
		System.out.println(dbAddress);
		Customer dbCustomer = findCustomerByTel(order.getCustomer().getTel());

		if (dbCustomer != null) {
			dbCustomer.setFirstName(order.getCustomer().getFirstName());
			dbCustomer.setLastName(order.getCustomer().getLastName());
			dbCustomer.setEmail(order.getCustomer().getEmail());
			List<Address> dbAddressList = dbCustomer.getAddressList();

			// customer 1 - address 1
			if (dbAddressList.stream().anyMatch(a -> a.getStreet().equals(orderAddress.getStreet())
					&& a.getStreetNr() == orderAddress.getStreetNr() && a.getGate().equals(orderAddress.getGate())
					&& a.getStaircase().equals(orderAddress.getStaircase())
					&& a.getFloor().equals(orderAddress.getFloor()) && a.getDoor().equals(orderAddress.getDoor()))) {

				order.setCustomer(dbCustomer);

				// customer 1 - address 0
			} else {
				dbCustomer.addAddress(orderAddress);
				order.setCustomer(dbCustomer);
			}
			// customer 0 - address 1
		} else if (dbCustomer == null && dbAddress != null) {
			order.getCustomer().getAddressList().clear();
			order.getCustomer().getAddressList().add(dbAddress);
		}
		// customer 0 - address 0
		em.persist(order);
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

	public List<Order> getOrders() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> getOrdersByStore(Long storeId) {
		TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.storePickUpId=:storeId", Order.class);
		query.setParameter("storeId", storeId);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> getOrdersByCustomer(Long customerId) {
		TypedQuery<Order> query = em.createQuery("from Order o WHERE o.customer.id=:customerId", Order.class);
		query.setParameter("customerId", customerId);
		List<Order> customerOrders = query.getResultList();
		return customerOrders;
	}

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
}
*/
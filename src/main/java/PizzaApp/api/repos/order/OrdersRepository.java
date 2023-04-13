package PizzaApp.api.repos.order;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Customer;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.exceptions.ChangeRequestedNotValidException;
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
	public void createOrUpdate(Order order) {
		String orderDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));

		Optional<Address> dbAddress = Optional.ofNullable(findAddress(order.getAddress().getStreet(),
				order.getAddress().getStreetNr(), order.getAddress().getGate(), order.getAddress().getStaircase(),
				order.getAddress().getFloor(), order.getAddress().getDoor()));

		Optional<Customer> dbCustomer = Optional.ofNullable(findCustomerByTel(order.getCustomer().getTel()));

		// create
		if (order.getId() == 0) {

			// set the current date
			order.getOrderDetails().setOrderDate(orderDate);

			// if customer is already in db, set the id from the db
			// to not insert the same customer again
			if (dbCustomer.isPresent()) {
				order.getCustomer().setId(dbCustomer.get().getId());
			}

			// check whatever order has an address
			if (order.getAddress() != null) {

				// if it is already in db, set its id
				// to not insert the same address again with a diff id
				if (dbAddress.isPresent()) {
					order.getAddress().setId(dbAddress.get().getId());
				}
			}
		} else {
			// update
			Order dbOrder = findById(order.getId());

			// if orderDetails is null
			// set them from DB to be able to set order update date
			if (order.getOrderDetails() == null) {
				order.setOrderDetails(dbOrder.getOrderDetails());
				order.getOrderDetails().setOrderDate(orderDate);
			} else {
				order.getOrderDetails().setOrderDate(orderDate);
			}

			// UPDATING DELIVERY
			// there can be either an address (home delivery) or a storePickUpName
			// not both

			// check whatever order-to-update has an address
			if (order.getAddress() != null) {

				// case 1 update consists in changing order from storePickUp to home delivery

				// if the address is not in db
				// nullify the order's id with which it was fetched (in UpdateOrderPage)
				// to insert the address, not overwrite the "original"
				if (dbAddress.isEmpty()) {
					order.getAddress().setId(null);
				} else {
					// if it is in db, set the id to not insert it again
					order.getAddress().setId(dbAddress.get().getId());
				}

				// and nullify storePickUp
				if (dbOrder.getStorePickUpName() != null) {
					order.setStorePickUpName(null);
				}
			}

			// case 2 update consists in changing order from home delivery to store pickup
			// set order's address to null so order persists to db with null address_id col
			if (order.getStorePickUpName() != null && dbOrder.getAddress() != null) {
				order.setAddress(null);
			}

			// case 3 neither the address or store pick up is being updated
			if (order.getAddress() == null && order.getStorePickUpName() == null) {
				// set the address if there is no storePickUp
				if (dbOrder.getStorePickUpName() == null) {
					order.setAddress(dbOrder.getAddress());
				} else {
					// set the storePickUp
					order.setStorePickUpName(dbOrder.getStorePickUpName());
				}
			}

			// UPDATING CUSTOMER

			// if not updating customer, set the same one from db
			if (order.getCustomer() == null) {
				order.setCustomer(dbOrder.getCustomer());
			} else {
				// if updating customer data with same tel number
				// set the dbCustomer id to order so there are no duplicate tel numbers
				if (dbCustomer.isPresent() && dbCustomer.get().getTel() == order.getCustomer().getTel()) {
					order.getCustomer().setId(dbCustomer.get().getId());
				}
			}
		}

		// validate change requested
		isChangeRequestedValid(order);

		// set the change to give back to the client
		order.getOrderDetails().setPaymentChange(calculatePaymentChange(order));

		// persist the order
		Order theOrder = em.merge(order);

		// set the newly generated id in the DB to return it to front-end
		order.setId(theOrder.getId());
	}

	// read

	public Order findById(Long id) {
		Order order = em.find(Order.class, id);
		if (order != null) {
			return order;
		} else {
			throw new NoResultException("Pedido " + id + " no se pudo encontrar");
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
	public void deleteById(Long id) {
		Order dbOrderToDelete = findById(id);
		em.remove(dbOrderToDelete);
	}

	// currently not in use

	public List<Order> findAll() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> findAllByStore(String storeName) {
		TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.storePickUpName=:storeName", Order.class);
		query.setParameter("storeName", storeName);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> findAllByCustomer(Long customerId) {
		TypedQuery<Order> query = em.createQuery("from Order o WHERE o.customer.id=:customerId", Order.class);
		query.setParameter("customerId", customerId);
		List<Order> customerOrders = query.getResultList();
		return customerOrders;
	}

	// utility functions for OrderRepository

	// value of requested change has to be greater than
	// totalCost or totalOfferCost
	// same as in front-end
	public boolean isChangeRequestedValid(Order order) {
		// if change requested == null, then it goes into DB as 0
		// so OK
		if (order.getOrderDetails().getChangeRequested() == null) {
			return true;

		} else if (order.getCart().getTotalCostOffers() > 0
				&& order.getOrderDetails().getChangeRequested() > order.getCart().getTotalCostOffers()) {
			return true;

		} else if (order.getCart().getTotalCostOffers() == 0
				&& order.getOrderDetails().getChangeRequested() > order.getCart().getTotalCost()) {
			return true;

		} else {
			// here got to throw custom error and handle it in GlobalExceptionHandler
			throw new ChangeRequestedNotValidException(
					"El valor del cambio de efectivo solicitado no puede ser menor o igual "
							+ "que el total/total con ofertas");
		}
	}

	// calculate totalCost or totalCostOffers - changeRequested
	// being the change to give back to client
	public Double calculatePaymentChange(Order order) {
		// check whatever user introduced any change request
		if (order.getOrderDetails().getChangeRequested() != null) {

			// if yes and there is a totalCostOffers
			if (order.getCart().getTotalCostOffers() > 0) {
				// return the calculation
				return order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCostOffers();

			} else {
				// if yes and there is no totalCostOffers, just totalCost
				// return the calculation
				return order.getOrderDetails().getChangeRequested() - order.getCart().getTotalCost();
			}
		} else {
			// if user did not introduce any change request
			// return 0 for the db
			return 0.0;
		}
	}
}

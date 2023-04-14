package PizzaApp.api.repos.order;

public class OrderRepoWorks {

	/*
	package PizzaApp.api.repos.order;

	import java.time.ZonedDateTime;
	import java.time.format.DateTimeFormatter;
	import java.util.List;
	import java.util.Optional;
	import org.springframework.stereotype.Repository;
	import PizzaApp.api.entity.clients.Address;
	import PizzaApp.api.entity.clients.customer.Telephone;
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

			// check for address and customer separately
			// to know whatever they're already in DB
			Optional<Address> dbAddress = Optional.ofNullable(findAddress(order));
			Optional<Telephone> dbCustomerTel = Optional.ofNullable(findCustomerTel(order));

			// create
			if (order.getId() == 0) {

				// set the current date
				order.getOrderDetails().setOrderDate(orderDate);

				// if customer tel is already in db, set the id from the db
				// to not insert the same customer tel again
				if (dbCustomerTel.isPresent()) {
					order.getCustomer().getTel().setId(dbCustomerTel.get().getId());
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

				// get original order, order with data before update
				Order originalOrder = findById(order.getId());

				// UPDATING CUSTOMER

				// if not updating customer, set the same one from db
				if (order.getCustomer() == null) {
					order.setCustomer(originalOrder.getCustomer());
				} else {
					// ensure that 2 customers can't have the same tel numbers
					// if by chance, when updating, the tel-to-update (new tel) is already in the db
					if (dbCustomerTel.isPresent()) {
						// if it is, assign the id of the tel already in db
						order.getCustomer().getTel().setId(dbCustomerTel.get().getId());
					} else {
						// if it's not in the db already, then nullify the id set in front-end 
						// so an INSERT happens
						order.getCustomer().getTel().setId(null);
					}
				}

				// UPDATING DELIVERY
				// there can be either an address (home delivery) or a storePickUpName
				// not both

				// check whatever order-to-update is updating address
				if (order.getAddress() != null) {

					// case 1 update consists in changing order from storePickUp to address (home
					// delivery)

					// if the address is not in db
					// nullify the id set on the front-end
					// to insert the address, not overwrite the original
					// if it is in db, the id set on the front end will correctly update
					if (dbAddress.isEmpty()) {
						order.getAddress().setId(null);
					}

					// nullify storePickUp if in the DB there was one
					if (originalOrder.getStorePickUpName() != null) {
						order.setStorePickUpName(null);
					}
				}

				// case 2 update consists in changing order from address (home delivery) to
				// storePickUp
				if (order.getStorePickUpName() != null && originalOrder.getAddress() != null) {
					// set order's address to null to nullify address_id col in db
					// since now the storePickUp col will have data
					order.setAddress(null);
				}

				// case 3 neither the address or store pick up is being updated
				if (order.getAddress() == null && order.getStorePickUpName() == null) {
					// set the address if there is no storePickUp
					if (originalOrder.getStorePickUpName() == null) {
						order.setAddress(originalOrder.getAddress());
					} else {
						// set the storePickUp
						order.setStorePickUpName(originalOrder.getStorePickUpName());
					}
				}

				// UPDATING ORDER DETAILS

				// check whatever order-to-update has orderDetails
				if (order.getOrderDetails() == null) {
					// if null (not updating) set from db
					order.setOrderDetails(originalOrder.getOrderDetails());
					// set new date to denote update date
					order.getOrderDetails().setOrderDate(orderDate);
				} else {
					// if it isn't null, then it has the id from the front-end
					// so it will UPDATE
					order.getOrderDetails().setOrderDate(orderDate);
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

		public Address findAddress(Order order) {

			Address dbAddress = null;

			if (order.getAddress() != null) {

				TypedQuery<Address> query = em.createQuery(
						"from Address a where a.street=:oStreet " + "and a.streetNr=:oNumber " + "and a.gate=:oGate "
								+ "and a.staircase=:oStaircase " + "and a.floor=:oFloor " + "and a.door=:oDoor",
						Address.class);

				query.setParameter("oStreet", order.getAddress().getStreet());
				query.setParameter("oNumber", order.getAddress().getStreetNr());
				query.setParameter("oGate", order.getAddress().getGate());
				query.setParameter("oStaircase", order.getAddress().getStaircase());
				query.setParameter("oFloor", order.getAddress().getFloor());
				query.setParameter("oDoor", order.getAddress().getDoor());

				try {
					dbAddress = query.getSingleResult();
				} catch (NoResultException nre) {
				}
			}
			return dbAddress;
		}

		public Telephone findCustomerTel(Order order) {

			Telephone dbTelephone = null;

			if (order.getCustomer() != null) {
				TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
				query.setParameter("number", order.getCustomer().getTel().getNumber());

				try {
					dbTelephone = query.getSingleResult();
				} catch (NoResultException nre) {
				}
			}
			return dbTelephone;
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

	*/
}

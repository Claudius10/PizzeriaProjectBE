package PizzaApp.api.services.order;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.order.OrdersRepository;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.customer.CustomerService;
import PizzaApp.api.services.telephone.TelephoneService;
import PizzaApp.api.utility.order.OrderUtilityMethods;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersService {

	private OrdersRepository orderRepository;
	private AddressService addressService;
	private TelephoneService telephoneService;
	private CustomerService customerService;
	private OrderUtilityMethods orderUtilityMethods = new OrderUtilityMethods();

	public OrdersService(OrdersRepository ordersRepo, AddressService addressService, TelephoneService telephoneService,
			CustomerService customerService) {
		this.orderRepository = ordersRepo;
		this.addressService = addressService;
		this.telephoneService = telephoneService;
		this.customerService = customerService;
	}

	public void createOrUpdate(Order order) {

		String orderDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));

		// check for address, customer, tel separately
		Optional<Address> dbAddress = Optional.ofNullable(addressService.findAddress(order));
		Optional<Telephone> dbCustomerTel = Optional.ofNullable(telephoneService.findCustomerTel(order));
		Optional<Customer> dbCustomer = Optional.ofNullable(customerService.findCustomer(order));

		// create
		if (order.getId() == 0) {

			// set the current date
			order.getOrderDetails().setOrderDate(orderDate);

			// if customer tel is already in db, set the id from the db
			// to not insert the same customer tel again
			if (dbCustomerTel.isPresent()) {
				order.getCustomer().getTel().setId(dbCustomerTel.get().getId());
				// if customer is also in db
				// do not insert a new customer, use the same one
				if (dbCustomer.isPresent()) {
					order.getCustomer().setId(dbCustomer.get().getId());
				}
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

				// if the address is not in db nullify the id set on the front-end
				// to insert the address, not overwrite the original
				// if it is in db, the id set on the front end will correctly update
				// unless updating from storePickUp to address, in which case
				// have to manually set it (else block) cause on front-end it's not set
				if (dbAddress.isEmpty()) {
					order.getAddress().setId(null);
				} else {
					order.getAddress().setId(dbAddress.get().getId());
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
		orderUtilityMethods.isChangeRequestedValid(order);

		// set the change to give back to the client
		order.getOrderDetails().setPaymentChange(orderUtilityMethods.calculatePaymentChange(order));

		//
		orderRepository.createOrUpdate(order);
	}

	public Order findById(Long id) {

		Order order = orderRepository.findById(id);

		if (order != null) {
			return order;
		} else {
			throw new NoResultException("Pedido " + id + " no se pudo encontrar");
		}
	}

	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public List<Order> findAllByStore(String storeName) {
		return orderRepository.findAllByStore(storeName);
	}
}

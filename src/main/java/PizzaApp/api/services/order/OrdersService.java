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

		// get the current date and time
		String orderDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));

		// check for address, customer, tel 
		Optional<Address> dbAddress = Optional.ofNullable(addressService.findAddress(order));
		Optional<Telephone> dbCustomerTel = Optional.ofNullable(telephoneService.findByNumber(order));
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

			// if order has an address and it's is already in db, set its id
			// to not insert the same address again with a diff id
			if (order.getAddress() != null && dbAddress.isPresent()) {
				order.getAddress().setId(dbAddress.get().getId());
			}

		} else {
			// update

			// get original order data
			Order originalOrder = findById(order.getId());

			// CUSTOMER

			// if not updating customer, set the same one from db
			if (order.getCustomer() == null) {
				order.setCustomer(originalOrder.getCustomer());
			} else {
				// set id to update
				order.getCustomer().setId(originalOrder.getCustomer().getId());
				// if the tel-to-update (new tel) is already in the db
				// set the id to not insert
				if (dbCustomerTel.isPresent()) {
					order.getCustomer().getTel().setId(dbCustomerTel.get().getId());
				}
			}

			// DELIVERY

			// check whatever order-to-update is updating address
			// if it is, set its id
			if (order.getAddress() != null && dbAddress.isPresent()) {
				order.getAddress().setId(dbAddress.get().getId());

				// nullify storePickUp if in the DB there was one
				if (originalOrder.getStorePickUpName() != null) {
					order.setStorePickUpName(null);
				}
			}

			// if neither the address or store pick up is being updated
			if (order.getAddress() == null && order.getStorePickUpName() == null) {
				// set the address if there is no storePickUp
				if (originalOrder.getStorePickUpName() == null) {
					order.setAddress(originalOrder.getAddress());
				} else {
					// set the storePickUp
					order.setStorePickUpName(originalOrder.getStorePickUpName());
				}
			}

			// ORDER DETAILS

			// check whatever order-to-update has orderDetails
			if (order.getOrderDetails() == null) {
				// if null (not updating) set from db
				order.setOrderDetails(originalOrder.getOrderDetails());
			} else {
				// set the id
				order.getOrderDetails().setId(originalOrder.getOrderDetails().getId());
			}

			// set update date and time
			order.getOrderDetails().setOrderDate(orderDate);

			// CART

			// set the cart if cart is not being updated
			if (order.getCart() == null) {
				order.setCart(originalOrder.getCart());
			} else {
				// it it is being updated, set its id
				order.getCart().setId(originalOrder.getCart().getId());
			}
		}

		// validation
		orderUtilityMethods.isCartEmpty(order);
		orderUtilityMethods.isChangeRequestedValid(order);

		// set the value for the change to give back to the client
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

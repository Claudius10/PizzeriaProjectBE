package PizzaApp.api.services.customer;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.customer.CustomerRepository;

@Service
@Transactional
public class CustomerService {

	private CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer findCustomer(Order order) {

		Customer customer = null;
		if (order.getCustomer() != null) {
			customer = order.getCustomer();
		}

		return customerRepository.findCustomer(customer);
	}

	// for testing
	public List<Customer> findCustomers(Customer customer) {
		return customerRepository.findCustomers(customer);
	}
}
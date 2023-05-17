package PizzaApp.api.repos.customer;
import java.util.List;
import PizzaApp.api.entity.clients.customer.Customer;

public interface CustomerRepository {

	public Customer findCustomer(Customer customer);

	public List<Customer> findCustomers(Customer customer);
}

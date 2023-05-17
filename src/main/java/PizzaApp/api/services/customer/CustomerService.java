package PizzaApp.api.services.customer;
import java.util.List;
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.order.Order;

public interface CustomerService {

	public Customer findCustomer(Order order);

	public List<Customer> findCustomers(Customer customer);
}

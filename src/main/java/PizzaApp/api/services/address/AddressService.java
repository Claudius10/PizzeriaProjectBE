package PizzaApp.api.services.address;
import java.util.List;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.order.Order;

public interface AddressService {

	public Address findAddress(Order order);

	public List<Address> findAddresses(Address address);
}

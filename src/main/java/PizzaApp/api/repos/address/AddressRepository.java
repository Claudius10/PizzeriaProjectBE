package PizzaApp.api.repos.address;
import java.util.List;
import PizzaApp.api.entity.clients.Address;

public interface AddressRepository {

	public Address findAddress(Address address);
	
	public List<Address> findAddresses(Address address);
}

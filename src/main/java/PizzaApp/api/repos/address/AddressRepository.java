package PizzaApp.api.repos.address;
import PizzaApp.api.entity.clients.Address;

public interface AddressRepository {

	public Address findAddress(Address address);
}

package PizzaApp.api.repos.address;

import PizzaApp.api.entity.clients.Address;

public interface AddressRepository {

	Address findAddress(Address address);
}

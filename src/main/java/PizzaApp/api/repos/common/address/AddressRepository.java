package PizzaApp.api.repos.common.address;

import PizzaApp.api.entity.common.Address;

public interface AddressRepository {

	Address findAddress(Address address);
}

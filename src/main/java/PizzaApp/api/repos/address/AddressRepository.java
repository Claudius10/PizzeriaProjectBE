package PizzaApp.api.repos.address;

import PizzaApp.api.entity.user.common.Address;

public interface AddressRepository {

	Address findAddress(Address address);
}

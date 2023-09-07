package PizzaApp.api.services.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

	Address findReference(String id);

	Optional<Address> find(Address address);

	List<Address> findAllByUserId(String id);

	Long findUserAddressListSize(String id);
}

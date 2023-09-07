package PizzaApp.api.repos.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {

	Address findReference(String id);

	Optional<Address> findAddress(Address address);

	Optional<List<Address>> findByUserId(String id);

	Long findUserAddressListSize(String id);
}

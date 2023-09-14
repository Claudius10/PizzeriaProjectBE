package PizzaApp.api.repos.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {

	Address findReference(String id);

	Optional<Address> findAddress(Address address);

	List<Address> findAllByUserId(String id);

	Long findUserAddressListSize(String id);
}

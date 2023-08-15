package PizzaApp.api.services.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

	Address findReference(Long id);

	Optional<Address> findAddress(Address address);

	Optional<List<Address>> findByUserId(Long id);

	Long findUserAddressListSize(Long id);
}

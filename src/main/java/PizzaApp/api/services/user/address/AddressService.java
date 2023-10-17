package PizzaApp.api.services.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

	Address findReference(Long addressId);

	Optional<Address> find(Address address);

	List<Address> findAllByUserId(Long userId);

	Integer findUserAddressListSize(Long userId);
}

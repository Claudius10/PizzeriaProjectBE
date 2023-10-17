package PizzaApp.api.repos.user.address;

import PizzaApp.api.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {

	Address findReference(Long addressId);

	Optional<Address> findAddress(Address address);

	List<Address> findAllByUserId(Long userId);

	Integer findUserAddressListSize(Long userId);
}

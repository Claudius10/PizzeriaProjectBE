package org.pizzeria.api.services.address;

import org.pizzeria.api.entity.address.Address;

import java.util.Optional;

public interface AddressService {

	Long create(Address address);

	Optional<Address> findByExample(Address address);

	Address findReference(Long addressId);

	Optional<Address> findAddressById(Long addressId);
}
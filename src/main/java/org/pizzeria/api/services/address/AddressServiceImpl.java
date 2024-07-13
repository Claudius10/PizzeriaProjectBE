package org.pizzeria.api.services.address;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.repos.address.AddressRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public Address findReference(Long addressId) {
		return addressRepository.getReferenceById(addressId);
	}

	@Override
	public Optional<Address> findAddressById(Long addressId) {
		return addressRepository.findAddressById(addressId);
	}

	@Override
	public Long create(Address address) {
		Optional<Address> addressToSave = findByExample(address);
		if (addressToSave.isPresent()) {
			return addressToSave.get().getId();
		} else {
			return addressRepository.save(address).getId();
		}
	}

	public Optional<Address> findByExample(Address address) {
		// if address is already in db return it
		if (address.getId() != null) {
			return addressRepository.findById(address.getId());
		}

		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

		Example<Address> example = Example.of(address, matcher);
		return addressRepository.findOne(example);
	}
}
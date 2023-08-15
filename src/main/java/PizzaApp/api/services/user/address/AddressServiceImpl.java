package PizzaApp.api.services.user.address;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.repos.user.address.AddressRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public Address findReference(Long id) {
		return addressRepository.findReference(id);
	}

	@Override
	public Optional<Address> findAddress(Address address) {
		return addressRepository.findAddress(address);
	}

	@Override
	public Optional<List<Address>> findByUserId(Long id) {
		return addressRepository.findByUserId(id);
	}

	@Override
	public Long findUserAddressListSize(Long id) {
		return addressRepository.findUserAddressListSize(id);
	}
}

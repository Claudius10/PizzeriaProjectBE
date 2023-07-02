package PizzaApp.api.services.common.address;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.common.Address;
import PizzaApp.api.repos.common.address.AddressRepository;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public Address findAddress(Address address) {

		if (address != null) {
			return addressRepository.findAddress(address);
		} else {
			return null;
		}
	}
}

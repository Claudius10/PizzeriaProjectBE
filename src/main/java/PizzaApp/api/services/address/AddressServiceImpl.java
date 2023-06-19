package PizzaApp.api.services.address;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.user.common.Address;
import PizzaApp.api.repos.address.AddressRepository;

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

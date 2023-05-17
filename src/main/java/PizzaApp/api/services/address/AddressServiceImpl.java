package PizzaApp.api.services.address;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.address.AddressRepository;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	private AddressRepository addressRepository;

	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public Address findAddress(Order order) {
		Address address = null;
		if (order.getAddress() != null) {
			address = order.getAddress();
		}

		return addressRepository.findAddress(address);
	}

	// for testing
	@Override
	public List<Address> findAddresses(Address address) {
		return addressRepository.findAddresses(address);
	}
}

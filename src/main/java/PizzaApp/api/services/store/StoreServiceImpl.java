package PizzaApp.api.services.store;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.repos.resources.StoreRepository;
import PizzaApp.api.services.address.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;

	private final AddressService addressService;

	public StoreServiceImpl(StoreRepository storeRepository, AddressService addressService) {
		this.storeRepository = storeRepository;
		this.addressService = addressService;
	}

	@Override
	public void createStore(Long addressId, String name, Integer number, String schedule) {
		Address address = addressService.findReference(addressId);
		storeRepository.save(new Store(null, name, address, number, schedule));
	}

	@Override
	public List<Store> findAll() {
		return storeRepository.findAll();
	}
}
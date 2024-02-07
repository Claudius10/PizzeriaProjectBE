package PizzaApp.api.services.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.telephone.Telephone;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.UserDataRepository;
import PizzaApp.api.services.address.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

	private final UserDataRepository userDataRepository;

	private final AddressService addressService;

	public UserDataServiceImpl
			(UserDataRepository userDataRepository,
			 AddressService addressService) {
		this.userDataRepository = userDataRepository;
		this.addressService = addressService;
	}

	@Override
	public void createData(UserData userData) {
		userDataRepository.createData(userData);
	}

	@Override
	public UserData findReference(Long userId) {
		return userDataRepository.findReference(userId);
	}

	@Override
	public UserData findById(Long userId) {
		return userDataRepository.findById(userId);
	}

	@Override
	public void delete(Long userId) {
		userDataRepository.delete(userId);
	}

	@Override
	public void addTel(Long userId, Integer telephone) {
		UserData userData = findReference(userId);
		userData.addTel(new Telephone(telephone));
	}

	@Override
	public void removeTel(Long userId, Long telId) {
		UserData userData = findReference(userId);
		Optional<Telephone> telToRemove =
				userData.getTelephoneList()
						.stream()
						.filter(telephone1 -> telephone1.getId().equals(telId))
						.findFirst();
		telToRemove.ifPresent(userData::removeTel);
	}

	@Override
	public void addAddress(Long userId, Address address) {
		UserData userData = findReference(userId);
		Optional<Address> dbAddress = addressService.find(address);

		if (dbAddress.isPresent()) {
			userData.addAddress(dbAddress.get());
		} else {
			userData.addAddress(address);
		}
	}

	@Override
	public void removeAddress(Long userId, Long addressId) {
		UserData userData = findReference(userId);
		Optional<Address> addressToRemove =
				userData.getAddressList()
						.stream()
						.filter(address1 -> address1.getId().equals(addressId))
						.findFirst();
		addressToRemove.ifPresent(userData::removeAddress);
	}
}

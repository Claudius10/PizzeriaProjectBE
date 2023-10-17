package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.Telephone;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.exceptions.exceptions.user.MaxAddressListSizeException;
import PizzaApp.api.exceptions.exceptions.user.MaxTelListSizeException;
import PizzaApp.api.repos.user.account.UserDataRepository;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

	private final UserDataRepository userDataRepository;

	private final TelephoneService telephoneService;

	private final AddressService addressService;

	public UserDataServiceImpl
			(UserDataRepository userDataRepository,
			 TelephoneService telephoneService,
			 AddressService addressService) {
		this.userDataRepository = userDataRepository;
		this.telephoneService = telephoneService;
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
	public void addTel(Long userId, Integer telephone) {
		if (telephoneService.findUserTelListSize(userId) == 3) {
			throw new MaxTelListSizeException("Solo se permiten 3 números de teléfono almacenados");
		}

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
		if (addressService.findUserAddressListSize(userId) == 3) {
			throw new MaxAddressListSizeException("Solo se permiten 3 domicilios almacenados");
		}

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

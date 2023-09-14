package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
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
	public UserData findReference(String id) {
		return userDataRepository.findReference(id);
	}

	@Override
	public UserData findById(String id) {
		return userDataRepository.findById(id);
	}

	@Override
	public UserDataDTO findDTOById(String id) {
		return userDataRepository.findDTOById(id);
	}

	@Override
	public void addTel(String id, Integer telephone) {
		if (telephoneService.findUserTelListSize(id) == 3) {
			throw new MaxTelListSizeException("Solo se permiten 3 números de teléfono almacenados");
		}

		UserData userData = findReference(id);
		userData.addTel(new Telephone(telephone));
	}

	@Override
	public void removeTel(String id, Integer telephone) {
		UserData userData = findReference(id);
		Optional<Telephone> telToRemove =
				userData.getTelephoneList()
						.stream()
						.filter(telephone1 -> telephone1.getNumber().equals(telephone))
						.findFirst();
		telToRemove.ifPresent(userData::removeTel);
	}

	@Override
	public void addAddress(String id, Address address) {
		if (addressService.findUserAddressListSize(id) == 3) {
			throw new MaxAddressListSizeException("Solo se permiten 3 domicilios almacenados");
		}

		UserData userData = findReference(id);
		Optional<Address> dbAddress = addressService.find(address);

		if (dbAddress.isPresent()) {
			userData.addAddress(dbAddress.get());
		} else {
			userData.addAddress(address);
		}
	}

	@Override
	public void removeAddress(String id, Address address) {
		UserData userData = findReference(id);
		Optional<Address> addressToRemove =
				userData.getAddressList()
						.stream()
						.filter(address1 -> address1.entityEquals(address))
						.findFirst();
		addressToRemove.ifPresent(userData::removeAddress);
	}
}

package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.Telephone;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.exceptions.exceptions.user.InvalidPasswordException;
import PizzaApp.api.repos.user.account.AccountRepository;
import PizzaApp.api.services.user.address.AddressService;
import PizzaApp.api.services.user.telephone.TelephoneService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final AddressService addressService;

	private final TelephoneService telephoneService;

	private final PasswordEncoder bCryptEncoder;

	public AccountServiceImpl(
			AccountRepository userRepository,
			AddressService addressService,
			TelephoneService telephoneService,
			PasswordEncoder bCryptEncoder) {
		this.accountRepository = userRepository;
		this.addressService = addressService;
		this.telephoneService = telephoneService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public UserData findReference(String id) {
		return accountRepository.findReference(id);
	}

	@Override
	public UserData findDataById(Long id) {
		return accountRepository.findDataById(id).orElseThrow();
	}

	@Override
	public UserDataDTO findDataDTOById(Long id) {
		return accountRepository.findDataDTOById(id).orElseThrow();
	}

	@Override
	public void createData(UserData userData) {
		accountRepository.createData(userData);
	}

	@Override
	public void addTel(Long id, Integer telephone) {
		// TODO: handle max tel and address per user in globalExceptionHandler
		if (telephoneService.findUserTelListSize(id) == 3) {
			throw new RuntimeException("Solo se permiten 3 números de teléfono almacenados");
		}
		UserData userData = findDataById(id);
		userData.addTel(new Telephone(telephone));
	}

	@Override
	public void removeTel(Long id, Integer telephone) {
		UserData userData = findDataById(id);
		Telephone telToRemove = userData.getTelephoneList()
				.stream()
				.filter(telephone1 -> telephone1.getNumber().equals(telephone))
				.findFirst()
				.orElseThrow();
		userData.removeTel(telToRemove);
	}

	@Override
	public void addAddress(Long id, Address address) {
		if (addressService.findUserAddressListSize(id) == 3) {
			throw new RuntimeException("Solo se permiten 3 domicilios almacenados");
		}

		UserData userData = findDataById(id);

		Optional<Address> dbAddress = addressService.findAddress(address);

		if (dbAddress.isPresent()) {
			userData.addAddress(dbAddress.get());
		} else {
			userData.addAddress(address);
		}
	}

	@Override
	public void removeAddress(Long id, Address address) {
		UserData userData = findDataById(id);
		Address addressToRemove = userData.getAddressList()
				.stream()
				.filter(address1 -> address1.entityEquals(address))
				.findFirst()
				.orElseThrow();
		userData.removeAddress(addressToRemove);
	}

	@Override
	public void updateEmail(String id, EmailChangeDTO emailChangeDTO) {
		String encodedPassword = loadPassword(id);
		boolean pwMatch = bCryptEncoder.matches(emailChangeDTO.password(), encodedPassword);

		if (!pwMatch) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}

		accountRepository.updateEmail(Long.valueOf(id), emailChangeDTO.email());
	}

	@Override
	public void updatePassword(String id, PasswordChangeDTO passwordChangeDTO) {
		String encodedPassword = loadPassword(id);
		boolean pwMatch = bCryptEncoder.matches(passwordChangeDTO.currentPassword(), encodedPassword);

		if (!pwMatch) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}

		String newPassword = bCryptEncoder.encode(passwordChangeDTO.newPassword());
		accountRepository.updatePassword(Long.valueOf(id), newPassword);
	}

	@Override
	public void updateName(String id, NameChangeDTO nameChangeDTO) {
		// TODO - mby make an util function to check if pw match when changing acc info
		String encodedPassword = loadPassword(id);
		boolean pwMatch = bCryptEncoder.matches(nameChangeDTO.password(), encodedPassword);

		if (!pwMatch) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}

		accountRepository.updateName(id, nameChangeDTO.name());
	}

	@Override
	public void delete(String id, PasswordDTO passwordDTO) {
		String encodedPassword = loadPassword(id);
		boolean pwMatch = bCryptEncoder.matches(passwordDTO.password(), encodedPassword);

		if (!pwMatch) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}

		accountRepository.delete(id);
	}

	@Override
	public String loadPassword(String id) {
		return accountRepository.loadPassword(id);
	}
}

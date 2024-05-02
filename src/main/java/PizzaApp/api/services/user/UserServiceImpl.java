package PizzaApp.api.services.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.repos.user.projections.UserProjection;
import PizzaApp.api.services.address.AddressService;
import PizzaApp.api.services.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleService roleService;

	private final AddressService addressService;

	private final PasswordEncoder bCryptEncoder;

	public UserServiceImpl(
			UserRepository userRepository,
			RoleService roleService,
			AddressService addressService,
			PasswordEncoder bCryptEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.addressService = addressService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public Long create(RegisterDTO registerDTO) {
		Role userRole = roleService.findByName("USER");
		String encodedPassword = bCryptEncoder.encode(registerDTO.password());

		User user = new User.Builder()
				.withName(registerDTO.name())
				.withEmail(registerDTO.email())
				.withPassword(encodedPassword)
				.withRoles(userRole)
				.build();

		return userRepository.save(user).getId();
	}

	@Override
	public Set<Address> findAddressListById(Long userId) {
		return userRepository.findAddressListById(userId);
	}

	@Override
	public boolean addAddress(Long userId, Address address) {
		User user = findByIdWithAddressList(userId);
		Optional<Address> dbAddress = addressService.findByExample(address);

		if (user.getAddressList().size() == 3) {
			return false;
		}

		if (dbAddress.isPresent()) {
			user.addAddress(dbAddress.get());
		} else {
			user.addAddress(address);
		}

		return true;
	}

	@Override
	public void removeAddress(Long userId, Long addressId) {

	}

	@Override
	public UserProjection findDTOById(Long userId) {
		return userRepository.findUserById(userId);
	}

	@Override
	public User findReference(Long userId) {
		return userRepository.getReferenceById(userId);
	}

	@Override
	public void updateName(String password, Long userId, String name) {
		userRepository.updateUserName(userId, name);
	}

	@Override
	public void updateEmail(String password, Long userId, String email) {
		userRepository.updateUserEmail(userId, email);
	}

	@Override
	public void updateUserContactNumber(String password, Long userId, Integer contactNumber) {
		userRepository.updateUserContactNumber(userId, contactNumber);
	}

	@Override
	public void updatePassword(String password, Long userId, String newPassword) {
		String encodedPassword = bCryptEncoder.encode(newPassword);
		userRepository.updateUserPassword(userId, encodedPassword);
	}

	@Override
	public void deleteById(String password, Long userId) {
		userRepository.deleteById(userId);
	}

	// for internal use only

	@Override
	public User findByEmail(String userEmail) {
		Optional<User> user = userRepository.findByEmailWithRoles(userEmail);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UsernameNotFoundException("User with email " + userEmail + " not found.");
		}
	}

	@Override
	public User findByIdWithAddressList(Long userId) {
		return userRepository.findByIdWithAddressList(userId);
	}
}
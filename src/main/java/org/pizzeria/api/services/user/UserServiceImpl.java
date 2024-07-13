package org.pizzeria.api.services.user;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.dto.auth.RegisterDTO;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.entity.user.dto.UserDTO;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.services.address.AddressService;
import org.pizzeria.api.services.role.RoleService;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.pizzeria.api.utils.globals.ValidationResponses;
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
	public Long createUser(RegisterDTO registerDTO) {
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
	public String addUserAddress(Long userId, Address address) {
		User user = findUserOrThrow(userId);
		if (user.getAddressList().size() == 3) {
			return ValidationResponses.ADDRESS_MAX_SIZE;
		}

		Optional<Address> dbAddress = addressService.findByExample(address);
		if (dbAddress.isPresent()) {
			user.addAddress(dbAddress.get());
		} else {
			user.addAddress(address);
		}

		return null;
	}

	@Override
	public String removeUserAddress(Long userId, Long addressId) {
		String result = null;
		User user = findUserOrThrow(userId);

		Optional<Address> dbAddress = user.getAddressList()
				.stream()
				.filter(address1 -> address1.getId().equals(addressId))
				.findFirst();

		if (dbAddress.isPresent()) {
			user.removeAddress(dbAddress.get());
		} else {
			result = ValidationResponses.ADDRESS_NOT_FOUND;
		}

		return result;
	}

	@Override
	public void updateUserName(String password, Long userId, String name) {
		User user = findUserOrThrow(userId);
		user.setName(name);
	}

	@Override
	public void updateUserEmail(String password, Long userId, String email) {
		User user = findUserOrThrow(userId);
		user.setEmail(email);
	}

	@Override
	public void updateUserContactNumber(String password, Long userId, Integer contactNumber) {
		User user = findUserOrThrow(userId);
		user.setContactNumber(contactNumber);
	}

	@Override
	public void updateUserPassword(String password, Long userId, String newPassword) {
		String encodedPassword = bCryptEncoder.encode(newPassword);
		User user = findUserOrThrow(userId);
		user.setPassword(encodedPassword);
	}

	@Override
	public void deleteUserById(String password, Long userId) {
		User user = findUserOrThrow(userId);
		userRepository.deleteById(user.getId());
	}

	@Override
	public Optional<UserDTO> findUserDTOById(Long userId) {
		return userRepository.findUserById(userId);
	}

	@Override
	public User findUserReference(Long userId) {
		return userRepository.getReferenceById(userId);
	}

	@Override
	public Set<Address> findUserAddressListById(Long userId) {
		return userRepository.findUserAddressListById(userId);
	}

	// for internal use only

	@Override
	public User findUserOrThrow(Long userId) {
		Optional<User> dbUser = userRepository.findById(userId);
		if (dbUser.isEmpty()) {
			throw new UsernameNotFoundException(String.format(SecurityResponses.USER_NOT_FOUND, userId));
		} else {
			return dbUser.get();
		}
	}

	@Override
	public boolean existsById(Long userId) {
		return userRepository.existsById(userId);
	}
}
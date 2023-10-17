package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.exceptions.exceptions.user.InvalidPasswordException;
import PizzaApp.api.exceptions.exceptions.user.NonUniqueEmailException;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.repos.user.account.UserRepository;
import PizzaApp.api.services.user.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final OrderRepository orderRepository;

	private final RoleService roleService;

	private final UserDataService userDataService;

	private final PasswordEncoder bCryptEncoder;

	public UserServiceImpl
			(UserRepository userRepository,
			 OrderRepository orderRepository,
			 RoleService roleService,
			 UserDataService userDataService,
			 PasswordEncoder bCryptEncoder) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
		this.roleService = roleService;
		this.userDataService = userDataService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public void create(RegisterDTO registerDTO) {
		Role userRole = roleService.findByName("USER").get();
		String encodedPassword = bCryptEncoder.encode(registerDTO.password());

		UserData userData = new UserData();
		userData.setUser(new User.Builder()
				.withName(registerDTO.name())
				.withEmail(registerDTO.email())
				.withPassword(encodedPassword)
				.withRoles(userRole)
				.build());

		try {
			userDataService.createData(userData);
		} catch (DataIntegrityViolationException ex) {
			throw new NonUniqueEmailException("Una cuenta ya existe con el correo electrónico introducido. Si no recuerda la " +
					"contraseña, contacte con nosotros");
		}
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDTO findDTOById(Long userId) {
		return userRepository.findDTOById(userId);
	}

	@Override
	public User findReference(Long userId) {
		return userRepository.findReference(userId);
	}

	@Override
	public void updateName(Long userId, NameChangeDTO nameChangeDTO) {
		verifyPassword(userId, nameChangeDTO.password());
		userRepository.updateName(userId, nameChangeDTO.name());
	}

	@Override
	public void updateEmail(Long userId, EmailChangeDTO emailChangeDTO) {
		verifyPassword(userId, emailChangeDTO.password());
		userRepository.updateEmail(userId, emailChangeDTO.email());
	}

	@Override
	public void updatePassword(Long userId, PasswordChangeDTO passwordChangeDTO) {
		verifyPassword(userId, passwordChangeDTO.currentPassword());
		String encodedPassword = bCryptEncoder.encode(passwordChangeDTO.newPassword());
		userRepository.updatePassword(userId, encodedPassword);
	}

	@Override
	public void delete(Long userId, PasswordDTO passwordDTO) {
		verifyPassword(userId, passwordDTO.password());
		// remove user data from orders
		orderRepository.removeUserData(userId); // TODO - make a test for this functionality
		// delete account
		userRepository.delete(userId);
	}

	@Override
	public String loadPassword(Long userId) {
		return userRepository.loadPassword(userId);
	}

	// util method

	private void verifyPassword(Long userId, String password) {
		if (!bCryptEncoder.matches(password, loadPassword(userId))) {
			throw new InvalidPasswordException("La contraseña proporcionada no coincide con la contraseña almacenada");
		}
	}
}

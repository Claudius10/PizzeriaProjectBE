package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.account.UserRepository;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.services.user.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final OrderService orderService;

	private final RoleService roleService;

	private final UserDataService userDataService;

	private final PasswordEncoder bCryptEncoder;

	public UserServiceImpl
			(UserRepository userRepository,
			 OrderService orderService,
			 RoleService roleService,
			 UserDataService userDataService,
			 PasswordEncoder bCryptEncoder) {
		this.userRepository = userRepository;
		this.orderService = orderService;
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

		userDataService.createData(userData);
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
	public String updateName(Long userId, NameChangeDTO nameChangeDTO) {
		String result = verifyPassword(userId, nameChangeDTO.password());
		if (result != null) {
			return result;
		}
		userRepository.updateName(userId, nameChangeDTO.name());
		return null;
	}

	@Override
	public String updateEmail(Long userId, EmailChangeDTO emailChangeDTO) {
		String result = verifyPassword(userId, emailChangeDTO.password());
		if (result != null) {
			return result;
		}
		userRepository.updateEmail(userId, emailChangeDTO.email());
		return null;
	}

	@Override
	public String updatePassword(Long userId, PasswordChangeDTO passwordChangeDTO) {
		String result = verifyPassword(userId, passwordChangeDTO.currentPassword());
		if (result != null) {
			return result;
		}
		String encodedPassword = bCryptEncoder.encode(passwordChangeDTO.newPassword());
		userRepository.updatePassword(userId, encodedPassword);
		return null;
	}

	@Override
	public String delete(Long userId, PasswordDTO passwordDTO) {
		String result = verifyPassword(userId, passwordDTO.password());
		if (result != null) {
			return result;
		}
		// remove user data from orders
		orderService.removeUserData(userId); // TODO - make a test for this functionality
		// delete account
		userRepository.delete(userId);
		return null;
	}

	@Override
	public String loadPassword(Long userId) {
		return userRepository.loadPassword(userId);
	}

	// util method

	private String verifyPassword(Long userId, String password) {
		if (!bCryptEncoder.matches(password, loadPassword(userId))) {
			return "La contraseña proporcionada no coincide con la contraseña almacenada";
		}
		return null;
	}
}

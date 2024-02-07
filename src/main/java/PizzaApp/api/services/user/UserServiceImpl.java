package PizzaApp.api.services.user;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.services.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

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
		Role userRole = roleService.findByName("USER");
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
	public String findUserEmailById(Long userId) {
		return userRepository.findUserEmailById(userId);
	}

	@Override
	public void updateName(String password, Long userId, String name) {
		userRepository.updateName(userId, name);
	}

	@Override
	public void updateEmail(String password, Long userId, String email) {
		userRepository.updateEmail(userId, email);
	}

	@Override
	public void updatePassword(String password, Long userId, String newPassword) {
		String encodedPassword = bCryptEncoder.encode(newPassword);
		userRepository.updatePassword(userId, encodedPassword);
	}

	@Override
	public void delete(String password, Long userId) {
		// remove user data from orders
		orderService.removeUserData(userId);
		// delete UserData which cascades delete to User
		userDataService.delete(userId);
	}
}
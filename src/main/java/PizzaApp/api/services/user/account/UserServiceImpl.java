package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.EmailChangeDTO;
import PizzaApp.api.entity.dto.user.NameChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.exceptions.exceptions.user.InvalidPasswordException;
import PizzaApp.api.repos.user.account.UserRepository;
import PizzaApp.api.services.user.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleService roleService;

	private final UserDataService userDataService;

	private final PasswordEncoder bCryptEncoder;

	public UserServiceImpl
			(UserRepository userRepository,
			 RoleService roleService, UserDataService userDataService, PasswordEncoder bCryptEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.userDataService = userDataService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public void create(RegisterDTO registerDTO) {
		Optional<Role> userRole = roleService.findByName("USER");

		String encodedPassword = bCryptEncoder.encode(registerDTO.password());

		User user = userRepository.create(new User.Builder()
				.withName(registerDTO.name())
				.withEmail(registerDTO.email())
				.withPassword(encodedPassword)
				.withRoles(userRole.get())
				.build());

		UserData userData = new UserData();
		userData.setUser(user);
		userDataService.createData(userData);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findReference(String id) {
		return userRepository.findReference(id);
	}

	@Override
	public void updateName(String id, NameChangeDTO nameChangeDTO) {
		verifyPassword(id, nameChangeDTO.password());
		userRepository.updateName(id, nameChangeDTO.name());
	}

	@Override
	public void updateEmail(String id, EmailChangeDTO emailChangeDTO) {
		verifyPassword(id, emailChangeDTO.password());
		userRepository.updateEmail(id, emailChangeDTO.email());
	}

	@Override
	public void updatePassword(String id, PasswordChangeDTO passwordChangeDTO) {
		verifyPassword(id, passwordChangeDTO.currentPassword());
		userRepository.updatePassword(id, passwordChangeDTO.newPassword());
	}

	@Override
	public void delete(String id, PasswordDTO passwordDTO) {
		verifyPassword(id, passwordDTO.password());
		userRepository.delete(id);
	}

	@Override
	public String loadPassword(String id) {
		return userRepository.loadPassword(id);
	}

	private void verifyPassword(String id, String password) {
		if (!bCryptEncoder.matches(password, loadPassword(id))) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}
	}
}

package PizzaApp.api.services.user;

import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.RegisterDTO;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.services.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder encoder;

	public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.encoder = encoder;
	}

	@Override
	public void create(RegisterDTO registerDTO) {
		Role userRole = roleService.findByName("USER").orElseThrow();

		String encodedPassword = encoder.encode(registerDTO.getPassword());

		userRepository.create(new User.Builder()
				.withUsername(registerDTO.getEmail())
				.withPassword(encodedPassword)
				.withRoles(userRole)
				.build());
	}
}

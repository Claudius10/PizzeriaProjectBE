package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.account.AnonUserRepository;
import PizzaApp.api.services.user.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnonUserServiceImpl implements AnonUserService {

	private final AnonUserRepository anonAccRepo;

	private final UserDataService userDataService;

	private final RoleService roleService;

	private final PasswordEncoder bCryptEncoder;

	public AnonUserServiceImpl(AnonUserRepository anonAccRepo, UserDataService userDataService, RoleService roleService, PasswordEncoder bCryptEncoder) {
		this.anonAccRepo = anonAccRepo;
		this.userDataService = userDataService;
		this.roleService = roleService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public void create(RegisterDTO registerDTO) {
		Role userRole = roleService.findByName("USER").orElseThrow();

		String encodedPassword = bCryptEncoder.encode(registerDTO.getPassword());

		User user = anonAccRepo.create(new User.Builder()
				.withName(registerDTO.getName())
				.withEmail(registerDTO.getEmail())
				.withPassword(encodedPassword)
				.withRoles(userRole)
				.build());

		UserData userData = new UserData();
		userData.setUser(user);
		userDataService.createData(userData);
	}
}

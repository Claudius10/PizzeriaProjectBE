package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.user.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.account.AnonAccRepo;
import PizzaApp.api.services.user.role.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnonAccServiceImpl implements AnonAccService {

	private final AnonAccRepo anonAccRepo;

	private final AccountService accountService;

	private final RoleService roleService;

	private final PasswordEncoder bCryptEncoder;

	public AnonAccServiceImpl(AnonAccRepo anonAccRepo, AccountService accountService, RoleService roleService, PasswordEncoder bCryptEncoder) {
		this.anonAccRepo = anonAccRepo;
		this.accountService = accountService;
		this.roleService = roleService;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public void create(RegisterDTO registerDTO) {
		Role userRole = roleService.findByName("USER").orElseThrow();

		String encodedPassword = bCryptEncoder.encode(registerDTO.getPassword());

		User user = anonAccRepo.create(new User.Builder()
				.withEmail(registerDTO.getEmail())
				.withPassword(encodedPassword)
				.withRoles(userRole)
				.build());

		UserData userData = new UserData(registerDTO.getName(), registerDTO.getEmail());
		userData.setUser(user);
		accountService.createData(userData);
	}
}

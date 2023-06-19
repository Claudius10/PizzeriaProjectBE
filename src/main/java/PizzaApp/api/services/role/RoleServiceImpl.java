package PizzaApp.api.services.role;

import PizzaApp.api.entity.user.Role;
import PizzaApp.api.repos.role.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void create(String roleName) {
		roleRepository.create(new Role(roleName));
	}

	@Override
	public Optional<Role> findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}
}
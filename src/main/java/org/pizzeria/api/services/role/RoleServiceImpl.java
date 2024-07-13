package org.pizzeria.api.services.role;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.repos.role.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void create(String roleName) {
		roleRepository.save(new Role(roleName));
	}

	@Override
	public Role findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}
}
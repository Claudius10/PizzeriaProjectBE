package org.pizzeria.api.services.role;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.repos.role.RoleRepository;
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
	public void createRole(Role role) {
		this.roleRepository.save(role);
	}

	@Override
	public Optional<Role> findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}
}
package org.pizzeria.api.services.role;

import org.pizzeria.api.entity.role.Role;

import java.util.Optional;

public interface RoleService {

	void createRole(Role role);

	Optional<Role> findByName(String roleName);
}
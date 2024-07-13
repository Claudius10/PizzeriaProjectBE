package org.pizzeria.api.services.role;

import org.pizzeria.api.entity.role.Role;

public interface RoleService {

	void create(String roleName);

	Role findByName(String roleName);
}

package PizzaApp.api.services.role;

import PizzaApp.api.entity.role.Role;

public interface RoleService {

	void create(String roleName);

	Role findByName(String roleName);
}

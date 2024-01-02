package PizzaApp.api.services.user.role;

import PizzaApp.api.entity.user.Role;

public interface RoleService {

	void create(String roleName);

	Role findByName(String roleName);
}

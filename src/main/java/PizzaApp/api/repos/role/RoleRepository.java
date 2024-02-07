package PizzaApp.api.repos.role;

import PizzaApp.api.entity.role.Role;

public interface RoleRepository {

	void create(Role role);

	Role findByName(String roleName);
}

package PizzaApp.api.repos.user.role;

import PizzaApp.api.entity.user.Role;

public interface RoleRepository {

	void create(Role role);

	Role findByName(String roleName);
}

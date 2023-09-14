package PizzaApp.api.repos.user.role;

import PizzaApp.api.entity.user.Role;

import java.util.Optional;

public interface RoleRepository {

	void create(Role role);

	Role findByName(String roleName);
}

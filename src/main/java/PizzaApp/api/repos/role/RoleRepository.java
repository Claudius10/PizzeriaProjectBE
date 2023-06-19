package PizzaApp.api.repos.role;

import PizzaApp.api.entity.user.Role;

import java.util.Optional;

public interface RoleRepository {

	void create(Role role);

	Optional<Role> findByName(String roleName);
}

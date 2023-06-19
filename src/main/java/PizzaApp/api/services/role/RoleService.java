package PizzaApp.api.services.role;

import PizzaApp.api.entity.user.Role;

import java.util.Optional;

public interface RoleService {

	void create(String roleName);

	Optional<Role> findByName(String roleName);
}

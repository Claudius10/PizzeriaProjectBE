package PizzaApp.api.services.user.role;

import PizzaApp.api.entity.user.Role;

import java.util.Optional;

public interface RoleService {

	void create(String roleName);

	Role findByName(String roleName);
}

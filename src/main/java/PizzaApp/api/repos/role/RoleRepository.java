package PizzaApp.api.repos.role;

import PizzaApp.api.entity.clients.user.Role;

public interface RoleRepository {

	Role findByName(String roleName);

}

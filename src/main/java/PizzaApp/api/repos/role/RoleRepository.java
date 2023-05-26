package PizzaApp.api.repos.role;
import PizzaApp.api.entity.clients.user.Role;

public interface RoleRepository {
	
	public Role findByName(String roleName);

}

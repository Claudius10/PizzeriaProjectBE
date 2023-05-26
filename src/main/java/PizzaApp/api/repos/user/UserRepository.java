package PizzaApp.api.repos.user;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.clients.Telephone;
import PizzaApp.api.entity.clients.user.User;

public interface UserRepository {
	
	public void create(Email email);
	public User findByTel(Telephone tel);
	public User findByEmail(Email email);
	public void deleteByTel(Telephone tel);
}

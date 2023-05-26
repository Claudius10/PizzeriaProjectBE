package PizzaApp.api.repos.email;
import PizzaApp.api.entity.clients.Email;

public interface EmailRepository {
	
	public Email findByAddress(String address);
}

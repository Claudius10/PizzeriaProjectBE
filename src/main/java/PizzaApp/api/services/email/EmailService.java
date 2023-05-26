package PizzaApp.api.services.email;
import PizzaApp.api.entity.clients.Email;

public interface EmailService {
	
	public Email findByAddress(Email email);
}

package PizzaApp.api.services.email;

import PizzaApp.api.entity.clients.Email;

public interface EmailService {

	Email findByAddress(Email email);
}

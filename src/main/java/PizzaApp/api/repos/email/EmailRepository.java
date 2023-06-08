package PizzaApp.api.repos.email;

import PizzaApp.api.entity.clients.Email;

public interface EmailRepository {

	Email findByAddress(String address);
}

package PizzaApp.api.services.common.email;

import PizzaApp.api.entity.common.Email;

public interface EmailService {

	Email findByAddress(Email email);
}

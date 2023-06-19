package PizzaApp.api.services.email;

import PizzaApp.api.entity.user.common.Email;

public interface EmailService {

	Email findByAddress(Email email);
}

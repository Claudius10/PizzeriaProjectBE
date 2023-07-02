package PizzaApp.api.repos.common.email;

import PizzaApp.api.entity.common.Email;

public interface EmailRepository {

	Email findByAddress(String address);
}

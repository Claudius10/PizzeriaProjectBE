package PizzaApp.api.repos.email;

import PizzaApp.api.entity.user.common.Email;

public interface EmailRepository {

	Email findByAddress(String address);
}

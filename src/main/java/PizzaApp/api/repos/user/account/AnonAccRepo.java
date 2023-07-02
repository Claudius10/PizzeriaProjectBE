package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;

public interface AnonAccRepo {

	User create(User user);
}

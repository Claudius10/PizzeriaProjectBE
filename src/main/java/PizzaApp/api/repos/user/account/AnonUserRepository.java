package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;

public interface AnonUserRepository {

	User create(User user);
}

package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;

public interface UserRepository {

	User findReference(String id);

	void updateName(String id, String name);

	void updateEmail(String id, String email);

	void updatePassword(String id, String password);

	String loadPassword(String id);

	void delete(String id);
}

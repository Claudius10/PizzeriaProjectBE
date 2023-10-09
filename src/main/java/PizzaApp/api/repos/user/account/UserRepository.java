package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;

import java.util.Optional;

public interface UserRepository {

	User create(User user);

	Optional<User> findByEmail(String email);

	User findReference(String id);

	void updateName(String id, String name);

	void updateEmail(String id, String email);

	void updatePassword(String id, String password);

	String loadPassword(String id);

	void delete(String id);
}

package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDTO;
import PizzaApp.api.entity.user.User;

import java.util.Optional;

public interface UserRepository {

	User create(User user);

	Optional<User> findByEmail(String email);

	UserDTO findDTOById(Long userId);

	User findReference(Long userId);

	void updateName(Long userId, String name);

	void updateEmail(Long userId, String email);

	void updatePassword(Long userId, String password);

	String loadPassword(Long userId);

	void delete(Long userId);
}

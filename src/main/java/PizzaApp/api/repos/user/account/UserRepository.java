package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDTO;
import PizzaApp.api.entity.user.User;

public interface UserRepository {

	User create(User user);

	UserDTO findDTOById(Long userId);

	User findReference(Long userId);

	void updateName(Long userId, String name);

	void updateEmail(Long userId, String email);

	void updatePassword(Long userId, String password);

	String loadPassword(Long userId);
}

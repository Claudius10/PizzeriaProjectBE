package PizzaApp.api.services.user;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.User;

public interface UserService {

	void create(RegisterDTO registerDTO);

	UserDTO findDTOById(Long userId);

	User findReference(Long userId);

	String findUserEmailById(Long userId);

	void updateName(String password, Long userId, String name);

	void updateEmail(String password, Long userId, String email);

	void updatePassword(String password, Long userId, String newPassword);

	void delete(String password, Long userId);
}

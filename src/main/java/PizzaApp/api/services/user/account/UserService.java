package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.User;

import java.util.Optional;

public interface UserService {

	void create(RegisterDTO registerDTO);

	Optional<User> findByEmail(String email);

	UserDTO findDTOById(Long userId);

	User findReference(Long userId);

	void updateName(Long userId, NameChangeDTO nameChangeDTO);

	void updateEmail(Long userId, EmailChangeDTO emailChangeDTO);

	void updatePassword(Long userId, PasswordChangeDTO passwordChangeDTO);

	void delete(Long userId, PasswordDTO passwordDTO);

	String loadPassword(Long userId);
}

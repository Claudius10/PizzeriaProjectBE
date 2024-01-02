package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.User;

public interface UserService {

	void create(RegisterDTO registerDTO);

	UserDTO findDTOById(Long userId);

	User findReference(Long userId);

	String updateName(Long userId, NameChangeDTO nameChangeDTO);

	String updateEmail(Long userId, EmailChangeDTO emailChangeDTO);

	String updatePassword(Long userId, PasswordChangeDTO passwordChangeDTO);

	String delete(Long userId, PasswordDTO passwordDTO);

	String loadPassword(Long userId);
}

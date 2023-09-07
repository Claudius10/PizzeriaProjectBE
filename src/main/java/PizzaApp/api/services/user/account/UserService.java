package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.EmailChangeDTO;
import PizzaApp.api.entity.dto.user.NameChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordDTO;

public interface UserService {

	void updateName(String id, NameChangeDTO nameChangeDTO);

	void updateEmail(String id, EmailChangeDTO emailChangeDTO);

	void updatePassword(String id, PasswordChangeDTO passwordChangeDTO);

	void delete(String id, PasswordDTO passwordDTO);

	String loadPassword(String id);
}

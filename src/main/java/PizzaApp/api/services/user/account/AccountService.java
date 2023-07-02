package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.UserData;

public interface AccountService {

	UserDataDTO findDataById(String id);

	void createData(UserData userData);

	void updateEmail(String id, String email);

	void updatePassword(String id, String password);
}

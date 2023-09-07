package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.UserData;

public interface UserDataRepository {

	void createData(UserData userData);

	UserData findReference(String id);

	UserData findById(String id);

	UserDataDTO findDTOById(String id);
}

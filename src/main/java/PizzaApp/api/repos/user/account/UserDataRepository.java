package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.UserData;

public interface UserDataRepository {

	void createData(UserData userData);

	UserData findReference(Long userId);

	UserData findById(Long userId);
}

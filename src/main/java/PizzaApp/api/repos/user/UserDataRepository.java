package PizzaApp.api.repos.user;

import PizzaApp.api.entity.user.UserData;

public interface UserDataRepository {

	void createData(UserData userData);

	UserData findReference(Long userId);

	UserData findById(Long userId);

	void delete(Long userId);
}

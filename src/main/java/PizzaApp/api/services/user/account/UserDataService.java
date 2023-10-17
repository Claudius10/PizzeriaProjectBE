package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.UserData;

public interface UserDataService {

	void createData(UserData userData);

	void addTel(Long userId, Integer telephone);

	void removeTel(Long userId, Long telId);

	void addAddress(Long userId, Address address);

	void removeAddress(Long userId, Long addressId);

	UserData findReference(Long userId);

	UserData findById(Long userId);
}

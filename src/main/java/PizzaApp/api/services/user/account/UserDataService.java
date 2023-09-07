package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.UserData;

public interface UserDataService {

	void createData(UserData userData);

	void addTel(String id, Integer telephone);

	void removeTel(String id, Integer telephone);

	void addAddress(String id, Address address);

	void removeAddress(String id, Address address);

	UserData findReference(String id);

	UserData findById(String id);

	UserDataDTO findDTOById(String id);

}
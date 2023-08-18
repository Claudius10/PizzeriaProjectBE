package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.*;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.user.Telephone;
import PizzaApp.api.entity.user.UserData;

public interface AccountService {

	UserData findReference(String id);

	UserData findDataById(Long id);

	UserDataDTO findDataDTOById(Long Id);

	void createData(UserData userData);

	void addTel(Long id, Integer telephone);

	void removeTel(Long id, Integer telephone);

	void addAddress(Long id, Address address);

	void removeAddress(Long id, Address address);

	void updateEmail(String id, EmailChangeDTO emailChangeDTO);

	void updatePassword(String id, PasswordChangeDTO passwordChangeDTO);

	void updateName(String id, NameChangeDTO nameChangeDTO);

	void delete(String id, PasswordDTO passwordDTO);

	String loadPassword(String id);
}

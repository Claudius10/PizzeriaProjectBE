package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;

import java.util.Optional;

public interface AccountRepository {

	User findUserReference(String id);

	UserData findReference(String id);

	Optional<UserData> findDataById(Long id);

	Optional<UserDataDTO> findDataDTOById(Long id);

	void createData(UserData userData);

	void updateEmail(Long id, String email);

	void updatePassword(Long id, String password);

	void updateName(String id, String name);

	void delete(String id);

	String loadPassword(String id);

}

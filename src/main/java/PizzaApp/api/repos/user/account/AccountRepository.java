package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.UserData;

import java.util.Optional;

public interface AccountRepository {

	Optional<UserDataDTO> findDataById(Long id);

	void createData(UserData userData);

	void updateEmail(Long id, String email);

	void updatePassword(Long id, String password);

}

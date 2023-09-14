package PizzaApp.api.repos.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;

public interface TelephoneRepository {

	List<TelephoneDTO> findAllByUserId(String id);

	Long findUserTelListSize(String id);
}

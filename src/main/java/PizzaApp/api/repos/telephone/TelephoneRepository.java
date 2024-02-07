package PizzaApp.api.repos.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;

public interface TelephoneRepository {

	List<TelephoneDTO> findAllByUserId(Long userId);

	Long findUserTelListSize(Long userId);
}

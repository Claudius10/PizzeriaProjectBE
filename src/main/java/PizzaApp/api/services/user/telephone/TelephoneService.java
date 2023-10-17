package PizzaApp.api.services.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;

public interface TelephoneService {

	List<TelephoneDTO> findAllByUserId(Long userId);

	Long findUserTelListSize(Long userId);
}

package PizzaApp.api.services.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;

public interface TelephoneService {

	List<TelephoneDTO> findAllByUserId(String id);

	Long findUserTelListSize(String id);
}

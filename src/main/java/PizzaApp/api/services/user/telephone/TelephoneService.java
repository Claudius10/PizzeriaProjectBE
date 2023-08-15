package PizzaApp.api.services.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;
import java.util.Optional;

public interface TelephoneService {

	Optional<List<TelephoneDTO>> findByUserId(Long id);

	Long findUserTelListSize(Long id);
}

package PizzaApp.api.repos.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;

import java.util.List;
import java.util.Optional;

public interface TelephoneRepository {

	Optional<List<TelephoneDTO>> findByUserId(Long id);

	Long findUserTelListSize(Long id);
}

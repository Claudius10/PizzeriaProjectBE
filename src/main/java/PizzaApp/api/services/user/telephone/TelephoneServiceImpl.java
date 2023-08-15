package PizzaApp.api.services.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.repos.user.telephone.TelephoneRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TelephoneServiceImpl implements TelephoneService {

	private final TelephoneRepositoryImpl telephoneRepository;

	public TelephoneServiceImpl(TelephoneRepositoryImpl telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@Override
	public Optional<List<TelephoneDTO>> findByUserId(Long id) {
		return telephoneRepository.findByUserId(id);
	}

	@Override
	public Long findUserTelListSize(Long id) {
		return telephoneRepository.findUserTelListSize(id);
	}
}
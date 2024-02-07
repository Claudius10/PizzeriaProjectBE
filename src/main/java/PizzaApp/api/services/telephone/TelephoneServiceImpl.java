package PizzaApp.api.services.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.repos.telephone.TelephoneRepositoryImpl;

import java.util.List;

@Service
@Transactional
public class TelephoneServiceImpl implements TelephoneService {

	private final TelephoneRepositoryImpl telephoneRepository;

	public TelephoneServiceImpl(TelephoneRepositoryImpl telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@Override
	public List<TelephoneDTO> findAllByUserId(Long userId) {
		return telephoneRepository.findAllByUserId(userId);
	}

	@Override
	public Long findUserTelListSize(Long userId) {
		return telephoneRepository.findUserTelListSize(userId);
	}
}
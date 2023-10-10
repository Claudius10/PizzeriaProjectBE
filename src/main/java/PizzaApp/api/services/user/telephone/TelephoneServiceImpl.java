package PizzaApp.api.services.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.repos.user.telephone.TelephoneRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TelephoneServiceImpl implements TelephoneService {

	private final TelephoneRepositoryImpl telephoneRepository;

	public TelephoneServiceImpl(TelephoneRepositoryImpl telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@Override
	public List<TelephoneDTO> findAllByUserId(String id) {
		return telephoneRepository.findAllByUserId(id);
	}

	@Override
	public Long findUserTelListSize(String id) {
		return telephoneRepository.findUserTelListSize(id);
	}
}
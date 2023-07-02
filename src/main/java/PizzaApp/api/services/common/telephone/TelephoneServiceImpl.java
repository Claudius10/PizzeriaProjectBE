package PizzaApp.api.services.common.telephone;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.common.Telephone;
import PizzaApp.api.repos.common.telephone.TelephoneRepositoryImpl;

@Service
@Transactional
public class TelephoneServiceImpl implements TelephoneService {

	private final TelephoneRepositoryImpl telephoneRepository;

	public TelephoneServiceImpl(TelephoneRepositoryImpl telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@Override
	public Telephone findByNumber(Telephone telephone) {
		try {
			return telephoneRepository.findByNumber(telephone.getNumber());
		} catch (NullPointerException e) {
			return null;
		}
	}
}
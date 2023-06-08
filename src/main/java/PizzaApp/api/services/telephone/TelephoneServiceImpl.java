package PizzaApp.api.services.telephone;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import PizzaApp.api.entity.clients.Telephone;
import PizzaApp.api.repos.telephone.TelephoneRepositoryImpl;

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
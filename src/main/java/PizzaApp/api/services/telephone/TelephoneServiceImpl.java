package PizzaApp.api.services.telephone;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.telephone.TelephoneRepositoryImpl;

@Service
@Transactional
public class TelephoneServiceImpl implements TelephoneService {

	private TelephoneRepositoryImpl telephoneRepository;

	public TelephoneServiceImpl(TelephoneRepositoryImpl telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	@Override
	public Telephone findByNumber(Order order) {
		Telephone telephone = null;

		if (order.getCustomer() != null) {
			telephone = order.getCustomer().getTel();
		}

		return telephoneRepository.findByNumber(telephone);
	}

	@Override
	public List<Telephone> findAllByNumber(Telephone telephone) {
		return telephoneRepository.findAllByNumber(telephone);
	}

}
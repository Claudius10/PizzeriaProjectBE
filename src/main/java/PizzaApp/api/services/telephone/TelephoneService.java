package PizzaApp.api.services.telephone;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.repos.telephone.TelephoneRepository;

@Service
@Transactional
public class TelephoneService {

	private TelephoneRepository telephoneRepository;

	public TelephoneService(TelephoneRepository telephoneRepository) {
		this.telephoneRepository = telephoneRepository;
	}

	public Telephone findByNumber(Order order) {

		Telephone telephone = null;

		if (order.getCustomer() != null) {
			telephone = order.getCustomer().getTel();
		}

		return telephoneRepository.findByNumber(telephone);
	}

	// for testing
	public List<Telephone> findAllByNumber(Telephone telephone) {
		return telephoneRepository.findAllByNumber(telephone);
	}
}
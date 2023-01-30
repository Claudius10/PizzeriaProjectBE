package PizzaApp.api.lab.OneToOne;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GeneralService {

	private Repo repo;

	public GeneralService(Repo repo) {
		this.repo = repo;
	}

	public OrderLab findOrderById(Long orderId) {
		return repo.findOrderById(orderId);
	}

	// read address uni
	public AddressLab findAddressById(Long addressId) {
		return repo.findAddressById(addressId);
	}

	// read address bi
	public AddressLabBi findBiAddressById(Long addressId) {
		return repo.findBiAddressById(addressId);
	}

	public List<OrderLab> findAllOrders() {
		return repo.findAllOrders();
	}

	public void createOrUpdateOrder(OrderLab order) {
		repo.createOrUpdateOrder(order);
	}

	public void deleteOrderById(Long orderId) {
		repo.deleteOrderById(orderId);
	}

}

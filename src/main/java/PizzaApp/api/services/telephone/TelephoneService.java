package PizzaApp.api.services.telephone;
import java.util.List;

import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;

public interface TelephoneService {

	public Telephone findByNumber(Order order);

	public List<Telephone> findAllByNumber(Telephone telephone);

}

package PizzaApp.api.services.telephone;

import PizzaApp.api.entity.clients.Telephone;

public interface TelephoneService {

	Telephone findByNumber(Telephone telephone);
}

package PizzaApp.api.repos.telephone;

import PizzaApp.api.entity.clients.Telephone;

public interface TelephoneRepository {

	Telephone findByNumber(int tel);
}
